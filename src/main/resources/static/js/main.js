(function($) {
    var request = $.ajax({'url': '/animals.json'});
    request.done(function (animals) {
        console.log(animals);
        var html = '';
        animals.forEach(function(animal) {
            html += '<div>';
            html += '<h1>' + animal.name + '</h1>';
            html += '<h4>' + animal.type + '</h4>';
            html += '<p>' + animal.size + '</p>';
            html += '</div>';
        });
        $('#root').html(html);
    });
})(jQuery);