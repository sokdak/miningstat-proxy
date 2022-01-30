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

function createDeviceStatTable(deviceCount, minerCount) {
    let deviceStatTable = document.getElementById("device_stat");
    let rows = deviceStatTable.rows;

    for (let i = deviceStatTable.rows.length - 2; i > 1; --i) {
        $(rows[i]).remove();
    }

    for (let i = 0; i < deviceCount + minerCount; ++i) {
        let row = deviceStatTable.insertRow(i + 2);

        for (let j = 0; j < 9; ++j) {
            row.insertCell(-1);
        }

        row.id = "GPU" + i;
        row.cells[0].className = "column_title";

        let temperatureCell = row.cells[4];
        temperatureCell.appendChild(document.createElement("span"));
        temperatureCell.appendChild(document.createElement("span"));
        temperatureCell.appendChild(document.createElement("span"));
    }
}

function temperatureColor(temperature, temperatureLimit) {
    let temperatureLimit1 = Math.floor(temperatureLimit * 0.9);
    let temperatureLimit2 = Math.floor(temperatureLimit * 0.8);
    if (temperature < temperatureLimit2)
        return "text_green";
    if (temperature < temperatureLimit1)
        return "text_yellow";
    return "text_red";
}

function updateStatistics(data) {
    var totalSpeed = 0;
    var totalSpeedPower = 0;
    var totalPowerUsage = 0;
    var totalAcceptedShares = 0;
    var totalStaleShares = 0;
    var totalInvalidShares = 0;

    var deviceStatTable = document.getElementById("device_stat");
    deviceStatTable.style.display = "table";

    var tableDeviceCount = 0;
    var totalDeviceCount = data.flatMap(x => x.devices).length;
    var totalMinerCount = data.length;

    for (let rigNo = 0; rigNo < totalMinerCount; rigNo++) {
        var gpuList = data[rigNo];

        // var statusCell = document.getElementById("status");
        // statusCell.innerText = "Online";
        // statusCell.className = "text_green";
        // document.getElementById("miner_name").innerText = data.miner;
        // document.getElementById("uptime").innerText = formatUptime(data.uptime);
        // document.getElementById("shares_per_minute").innerText = data.shares_per_minute; //.toFixed(2);
        // document.getElementById("algorithm").innerText = data.algorithm;
        // var electricityStr;
        // if (data.electricity !== 0) {
        //     electricityStr = data.electricity + " kWh";
        //     if (data.electricity_cost != null)
        //         electricityStr += " $" + data.electricity_cost.toFixed(2);
        // } else
        //     electricityStr = "N/A";
        // document.getElementById("electricity").innerText = electricityStr;
        // document.getElementById("server").innerText = (data.server == null ? "N/A" : data.server);
        // document.getElementById("user").innerText = (data.user == null ? "N/A" : data.user);

        for (var deviceIndex = 0; deviceIndex < gpuList.length; ++deviceIndex) {
            var device = gpuList.devices[deviceIndex];
            var row = document.getElementById("GPU" + (tableDeviceCount + deviceIndex));
            var cells = row.cells;

            cells[0].innerText = "J" + (rigNo + 1).toFixed(2);
            cells[1].innerText = device.gpu_id;
            cells[2].innerText = device.name;
            cells[3].innerText = device.fan + " %";

            if (device.temperature === 0) {
                cells[4].innerText = "N/A";
                cells[4].className = null;
            } else {
                var temperatureChilds = cells[4].childNodes;
                temperatureChilds[0].innerText = device.temperature + " C";
                temperatureChilds[0].className = temperatureColor(device.temperature, 70);

                if (device.memory_temperature !== 0) {
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

            if (device.power_usage !== 0) {
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
            } else {
                cells[8].innerText = "-";
            }
        }
        tableDeviceCount += gpuList.length;
    }

    if (tableDeviceCount === totalDeviceCount) {
        console.log("successfully load " + totalDeviceCount + " of devices.")
    }
    else {
        console.error(`some device cannot be loaded (${tableDeviceCount}/${totalDeviceCount})`);
    }

    var row = document.getElementById("total");
    var cells = row.cells;

    cells[5].innerText = formatValue(totalSpeed, data.speed_rate_precision, "H/s");

    if (totalPowerUsage !== 0) {
        cells[6].innerText = totalPowerUsage + " W";
        cells[7].innerText = formatValue((totalSpeedPower / totalPowerUsage), 2, "H/W");
    } else {
        cells[6].innerText = "N/A";
        cells[7].innerText = "N/A";
    }

    cells[8].innerText = totalAcceptedShares + " / " + totalStaleShares + " / " + totalInvalidShares;
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

var requestTime = null;

async function ajax() {
    var resp = null;

    await $.ajaxSetup({
        headers: {
            'X-Auth-Key': be_key
        }
    });
    await $.getJSON(be_endpoint + "/miner", function (data) {
        if (data == null) {
            return;
        }
        resp = data;
    }).fail(function(){console.log("error");});

    return resp;
}

async function onLoad() {
    requestTime = (new Date).getTime();

    var data = await ajax();
    var totalMinerCount = data.length;
    var totalDeviceCount = data.flatMap(x => x.devices).length;

    if (totalDeviceCount !== 0) {
        createDeviceStatTable(totalDeviceCount, totalMinerCount);
    }

    updateStatistics(data);
    var currentTime = (new Date).getTime();
    var delay = 1000 - (currentTime - requestTime);
    setTimeout(onLoad, delay > 0 ? delay : 1);
}

window.onload = onLoad;

const be_endpoint = "";
const be_key = "dhqjzmffjr1!";
