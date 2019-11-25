(function($) {
    $("#myInput").on("keyup", function() {
        let value = $(this).val().toLowerCase();
        $(".card").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });


    $(".myInput2 a").click(function() {
        let value = $(this).text().toLowerCase();
            $(".card").filter(function() {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
    });




    // Google Map with Marker //
    let map;
    function initMap() {
        const sanAntonio = {lat: 29.424, lng: -98.4936}
        map = new google.maps.Map(document.getElementById('map'), {
            center: sanAntonio,
            zoom: 10
        });
        let marker = new google.maps.Marker({position: sanAntonio, map: map});
    }

    var gMap = false;





})(jQuery);








// Dynamic HTML for JSON

// let request = $.ajax({'url': '/animals.json'});
// request.done(function (animals) {
//     let html = '';
//     animals.forEach(function(animal) {
//         html += `<div class="card" style="width: 18rem;">
//                     <img height="150" width="150" class="card-img-top" alt="">
//                     <div class="card-body">
//                         <h5 class="card-title">${animal.name}</h5>
//                         <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's
//                 content.</p>
//                         <a href="" class="btn">VIEW PET</a>
//                         <a href="" class="btn">LIKE PET</a>
//                     </div>
//                 </div>`
//     });
//     $('#new-card').html(html);
// });