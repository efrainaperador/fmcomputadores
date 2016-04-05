fmcomputadores.controller('productCtrl', ['$scope', '$http', '$location', function($scope, $http, $location){
	var id = $location.search().id;
	$http.get('/tienda-1.0/webresources/producto/' + id).then(function(response){
		$scope.product = response.data;
	}, function(error){

	});
}]);