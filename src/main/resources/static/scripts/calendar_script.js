$(function() {
    var data = [
        { "date": "2018-04-21 10:15:20", "title": "Событие 1", "description": "Lorem Ipsum dolor set", "url": "http://www.event3.com/" },

        { "date": "2018-05-21 10:15:20", "title": "Событие 2", "description": "Lorem Ipsum dolor set", "url": "" },

        { "date": "2018-05-01 10:15:20", "title": "Событие 3", "description": "Lorem Ipsum dolor set", "url": "http://www.event3.com/" },

        { "date": "2018-05-06 10:15:20", "title": "Событие 4", "description": "Lorem Ipsum dolor set", "url": "http://www.event3.com/" }
    ];
    // page is now ready, initialize the calendar...

    $('#calendar').eventCalendar({
        jsonData: data,
        jsonDateFormat: 'human'
    });

});