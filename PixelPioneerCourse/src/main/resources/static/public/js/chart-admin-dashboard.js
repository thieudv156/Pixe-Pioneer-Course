"use strict";

// Ensure moment-range is extended properly
window["moment-range"].extendMoment(moment);

// Initialize variables
var t, r = [], n = [], o = [];
var startDate = moment().subtract(6, "days").format("YYYY-MM-DD");
var endDate = moment().format("YYYY-MM-DD");
var dateRange = Array.from(moment.range(startDate, endDate).by("days"));

// Populate datasets and colors
dateRange.forEach(function(date) {
    r.push({
        y: Math.floor(300 * Math.random()) + 30,
        x: date.toDate()
    });
    n.push({
        y: Math.floor(300 * Math.random()) + 10,
        x: date.toDate()
    });
    o.push(date.startOf("day").isSame(moment().startOf("day")) ? settings.colors.accent[500] : settings.colors.primary[500]);
});

// Function to create the chart
function createChart(elementId, chartType, options) {
    var defaultOptions = {
        barRoundness: 1.2,
        title: {
            display: true,
            fontSize: 12,
            fontColor: "rgba(54, 76, 102, 0.54)",
            position: "top",
            text: "GENERATED INCOME"
        },
        scales: {
            yAxes: [{ ticks: { maxTicksLimit: 4 } }],
            xAxes: [{
                offset: true,
                ticks: { padding: 10 },
                gridLines: { display: false },
                type: "time",
                time: { unit: "day", displayFormats: { day: "D/MM" }, maxTicksLimit: 7 }
            }]
        }
    };

    // Merge custom options with default options
    options = Chart.helpers.merge(defaultOptions, options);

    // Define the datasets
    var data = {
        datasets: [
            { label: "Previous Week", data: n, barThickness: 20, backgroundColor: 'rgba(75, 192, 192, 0.2)', borderColor: 'rgba(75, 192, 192, 1)', borderWidth: 1 },
            { label: "Last Week", data: r, barThickness: 20, backgroundColor: 'rgba(153, 102, 255, 0.2)', borderColor: 'rgba(153, 102, 255, 1)', borderWidth: 1 }
        ]
    };

    // Create the chart
    var ctx = document.querySelector(elementId).getContext('2d');
    new Chart(ctx, {
        type: chartType,
        data: data,
        options: options
    });
}

createChart("#earningsChart", 'bar', {});
