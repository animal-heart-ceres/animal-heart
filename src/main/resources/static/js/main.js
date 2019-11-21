(function($) {
    var request = $.ajax({'url': '/animals.json'});
    request.done(function (animals) {
        console.log(animals);
        var html = '';
        animals.forEach(function(animal) {
            html += '<div>';
            html += '<h1>' + animal.name + '</h1>';
            html += '</div>';
        });
        $('#root').html(html);
    });
})(jQuery);