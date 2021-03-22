
module.exports = function (runtime, scope) {
    var rtStatistics = runtime.statistics;
    var statistics = {};

    statistics.set = function (key, value) {
        rtStatistics.set(key, value);
    }

    statistics.show = rtStatistics.show.bind(rtStatistics);

    return statistics;
}