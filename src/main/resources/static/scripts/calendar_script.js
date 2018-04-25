$(function() {

    // page is now ready, initialize the calendar...

    $('#calendar').fullCalendar({
        defaultView: 'month',
        fixedWeekCount: false,
        height: 600,
        header:{
            left:   'title',
            center: 'prev,next',
            right:  'today'
        }
    });

});