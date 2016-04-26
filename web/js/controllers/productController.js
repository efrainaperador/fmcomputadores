fmcomputadores.controller('productCtrl', ['$scope', '$http', '$location', function($scope, $http, $location){
	var id = $location.search().id;
	$http.get('/tienda-1.0/webresources/producto/' + id).then(function(response){
		$scope.product = response.data;
		$scope.product.especificaciones = $scope.product.especificacion.split(/\n/g);
		$('.carousel').lightSlider({
	        gallery:true,
	        item:1,
	        loop:true,
	        thumbItem:9,
	        slideMargin:0,
	        enableDrag: false,
	        currentPagerPosition:'left',
	        onSliderLoad: function(el) {
	            el.lightGallery({
	                selector: '.carousel .lslide'
	            });
	        }   
	    }); 
	}, function(error){

	});

	$scope.changeImage = function(){

	}
}]);