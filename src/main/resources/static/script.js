function formatUptime(uptime) {
    var seconds = uptime % 60;
    uptime /= 60;
    var minutes = uptime % 60;
    uptime /= 60;
    var hours = uptime % 24;
    uptime /= 24;
    var days = uptime;
    return (Math.floor(days) + "d "
        + ("0" + Math.floor(hours)).substr(-2) + ":"
        + ("0" + Math.floor(minutes)).substr(-2) + ":"
        + ("0" + Math.floor(seconds)).substr(-2));
}

function formatValue(value, defaultPrecision, valueUnit) {
    if (value >= 1000000000)
        return ((value / 1000000000).toFixed(2) + " G" + valueUnit);
    else if (value >= 1000000)
        return ((value / 1000000).toFixed(2) + " M" + valueUnit);
    else if (value >= 1000)
        return ((value / 1000).toFixed(2) + " K" + valueUnit);
    else
        return (value.toFixed(defaultPrecision) + " " + valueUnit);
}

function createDeviceStatTable(deviceCount) {
    var deviceStatTable = document.getElementById("device_stat");
    var rows = deviceStatTable.rows;
    for (var i = deviceStatTable.rows.length - 2; i > 1; --i)
        $(rows[i]).remove();
    for (var i = 0; i < deviceCount + servers.length; ++i) {
        var row = deviceStatTable.insertRow(i + 2);
        for (var j = 0; j < 9; ++j)
            row.insertCell(-1);
        row.id = "GPU" + i;
        row.cells[0].className = "column_title";
        var temperatureCell = row.cells[4];
        temperatureCell.appendChild(document.createElement("span"));
        temperatureCell.appendChild(document.createElement("span"));
        temperatureCell.appendChild(document.createElement("span"));
    }
}

function temperatureColor(temperature, temperatureLimit) {
    var temperatureLimit1 = Math.floor(temperatureLimit * 0.9);
    var temperatureLimit2 = Math.floor(temperatureLimit * 0.8);
    if (temperature < temperatureLimit2)
        return "text_green";
    if (temperature < temperatureLimit1)
        return "text_yellow";
    return "text_red";
}

function updateStatistics(data) {
    var statusCell = document.getElementById("status");
    statusCell.innerText = "Online";
    statusCell.className = "text_green";
    document.getElementById("miner_name").innerText = data.miner;
    document.getElementById("uptime").innerText = formatUptime(data.uptime);
    document.getElementById("shares_per_minute").innerText = data.shares_per_minute; //.toFixed(2);
    document.getElementById("algorithm").innerText = data.algorithm;
    var electricityStr;
    if (data.electricity !== 0) {
        electricityStr = data.electricity + " kWh";
        if (data.electricity_cost != null)
            electricityStr += " $" + data.electricity_cost.toFixed(2);
    } else
        electricityStr = "N/A";
    document.getElementById("electricity").innerText = electricityStr;
    document.getElementById("server").innerText = (data.server == null ? "N/A" : data.server);
    document.getElementById("user").innerText = (data.user == null ? "N/A" : data.user);

    var totalSpeed = 0;
    var totalSpeed2 = 0;
    var totalSpeedPower = 0;
    var totalPowerUsage = 0;
    var totalAcceptedShares = 0;
    var totalAcceptedShares2 = 0;
    var totalStaleShares = 0;
    var totalStaleShares2 = 0;
    var totalInvalidShares = 0;
    var totalInvalidShares2 = 0;
    var dualMode = (data.total_accepted_shares2 != null);

    var deviceStatTable = document.getElementById("device_stat");
    deviceStatTable.style.display = "table";

    var rigNo = 0;

    for (var deviceIndex = 0; deviceIndex < data.devices.length; ++deviceIndex) {
        var device = data.devices[deviceIndex];
        var row = document.getElementById("GPU" + (deviceIndex + rigNo));
        var cells = row.cells;

        cells[0].innerText = server_names[rigNo];
        cells[1].innerText = device.gpu_id;
        cells[2].innerText = device.name;
        cells[3].innerText = device.fan + " %";
        if (device.temperature == 0) {
            cells[4].innerText = "N/A";
            cells[4].className = null;
        } else {
            var temperatureChilds = cells[4].childNodes;
            temperatureChilds[0].innerText = device.temperature + " C";
            temperatureChilds[0].className = temperatureColor(device.temperature, 70);
            if (device.memory_temperature != 0) {
                temperatureChilds[1].innerText = " / ";
                temperatureChilds[2].innerText = device.memory_temperature + " C";
                temperatureChilds[2].className = temperatureColor(device.memory_temperature, 105);
            } else {
                temperatureChilds[1].innerText = null;
                temperatureChilds[2].innerText = null;
            }
        }
        cells[5].innerText = formatValue(device.speed, data.speed_rate_precision, "H/s");
        totalSpeed += device.speed;
        if (dualMode) {
            cells[5].innerText += " + " + formatValue(device.speed2, data.speed_rate_precision2, data.speed_unit2);
            totalSpeed2 += device.speed2;
        }
        if (device.power_usage != 0) {
            cells[6].innerText = device.power_usage + " W";
            cells[7].innerText = formatValue((device.speed / device.power_usage), 2, "H/W");
            totalSpeedPower += device.speed;
            totalPowerUsage += device.power_usage;
        } else {
            cells[6].innerText = "N/A";
            cells[7].innerText = "N/A";
        }
        if (device.accepted_shares != null) {
            cells[8].innerText = device.accepted_shares + " / " + device.stale_shares + " / " + device.invalid_shares;
            totalAcceptedShares += device.accepted_shares;
            totalStaleShares += device.stale_shares;
            totalInvalidShares += device.invalid_shares;
            if (dualMode) {
                if (extendedShareInfo)
                    cells[8].innerText += " + " + device.accepted_shares2 + " / " + device.stale_shares2 + " / " + device.invalid_shares2;
                else
                    cells[8].innerText += " + " + device.accepted_shares2 + " / " + device.rejected_shares2;
                totalAcceptedShares2 += device.accepted_shares2;
                totalStaleShares2 += device.stale_shares2;
                totalInvalidShares2 += device.invalid_shares2;
            }
        } else
            cells[8].innerText = "-";


        if (deviceIndex + 1 < data.devices.length)
            if (data.devices[deviceIndex + 1].gpu_id === 0)
                rigNo++;

    }
    var row = document.getElementById("total");
    var cells = row.cells;
    cells[5].innerText = formatValue(totalSpeed, data.speed_rate_precision, "H/s");
    if (dualMode)
        cells[5].innerText += " + " + formatValue(totalSpeed2, data.speed_rate_precision2, data.speed_unit2);
    if (totalPowerUsage != 0) {
        cells[6].innerText = totalPowerUsage + " W";
        cells[7].innerText = formatValue((totalSpeedPower / totalPowerUsage), 2, "H/W");
    } else {
        cells[6].innerText = "N/A";
        cells[7].innerText = "N/A";
    }

    cells[8].innerText = totalAcceptedShares + " / " + totalStaleShares + " / " + totalInvalidShares;
    if (dualMode)
        cells[8].innerText += totalAcceptedShares2 + " / " + totalStaleShares2 + " / " + totalInvalidShares2;
}

function minerDisconnected() {
    var statusCell = document.getElementById("status");
    statusCell.innerText = "Offline";
    statusCell.className = "text_red";
    document.getElementById("miner_name").innerText = "Miner";
    document.getElementById("uptime").innerText = "N/A";
    document.getElementById("shares_per_minute").innerText = "N/A";
    document.getElementById("algorithm").innerText = "N/A";
    document.getElementById("electricity").innerText = "N/A";
    document.getElementById("server").innerText = "N/A";
    document.getElementById("user").innerText = "N/A";
    var deviceStatTable = document.getElementById("device_stat");
    deviceStatTable.style.display = "none";
}

function showError(message) {
    error = document.getElementById("error");
    error.innerText = message;
    error.style.display = "block";
    var minerStatTable = document.getElementById("miner_stat");
    minerStatTable.style.display = "none";
    var deviceStatTable = document.getElementById("device_stat");
    deviceStatTable.style.display = "none";
}

var httpRequest = null;
var requestTime = null;
var totalResponse = null;

async function ajax() {
    var devs = null;
    var lastResp = null;

    for (let i = 0; i < servers.length; i++) {
        await $.getJSON(servers[i], function (data) {
            if (data == null) {
                return;
            }
            if (devs == null) {
                devs = data.devices;
            } else {
                let tmp = data.devices;
                for (let j = 0; j < tmp.length; j++) {
                    devs.push(tmp[j])
                }
            }
            data.devices = devs;
            lastResp = data;
        }).fail(function(){console.log("error");});
    }

    totalResponse = lastResp;
}

async function onLoad() {
    var deviceStatTable = document.getElementById("device_stat");
    var deviceCount = 0;

    requestTime = (new Date).getTime();
    await ajax()
    var data = totalResponse;
    if (deviceCount !== data.devices.length) {
        deviceCount = data.devices.length;
        createDeviceStatTable(data.devices.length);
    }
    updateStatistics(data);
    var currentTime = (new Date).getTime();
    var delay = 1000 - (currentTime - requestTime);
    setTimeout(onLoad, delay > 0 ? delay : 1);
}

window.onload = onLoad;

const key = "dhqjzmffjr1!";

const TYPE_GMINER = "gminer";
const TYPE_TEAMREDMINER = "tredminer";
const TYPE_TREXMINER = "trexminer";

const PORT_TEAMREDMINER = 4028;
const PORT_GMINER = 10050;
const PORT_TREXMINER = 4067;

const server_names = ["j01-sd", "j02-sd", "j03-dy", "j04-dy", "j05-cm", "j06-cm", "j07-dy", "j08-dy", "j09-sd", "j10-dy", "j11-dy", "j12-ch", "j13-ch", "j14-sd", "j15-sd", "j16-ch", "j17-ch", "j18-ch", "j19-ch", "j20-dy", "j21-dy"]
const servers = ["/stat/miner?ip=192.168.1.70&port=10050&type=gminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.71&port=4028&type=tredminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.72&port=10050&type=gminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.73&port=10050&type=gminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.74&port=10050&type=gminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.75&port=10050&type=gminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.76&port=10050&type=gminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.77&port=4067&type=trexminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.78&port=10050&type=gminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.79&port=10050&type=gminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.80&port=4067&type=trexminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.81&port=4067&type=trexminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.82&port=4067&type=trexminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.83&port=10050&type=gminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.84&port=4067&type=trexminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.85&port=4067&type=trexminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.86&port=4067&type=trexminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.87&port=4067&type=trexminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.88&port=4067&type=trexminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.89&port=4067&type=trexminer&key=dhqjzmffjr1!", "/stat/miner?ip=192.168.1.90&port=4067&type=trexminer&key=dhqjzmffjr1!"];
