fmcomputadores.controller('productosCtrl', ['$scope', '$http', '$location', function($scope, $http, $location){
	$scope.products = [];

	$http.get('/tienda-1.0/webresources/producto').then(function(response){
		$scope.products = response.data;
	}, function(error){

	});

	$scope.loadProduct = function(evt){
		var id = $(evt.currentTarget).data('id');
		$location.path('/product').search('id='+id);
	}
}]);