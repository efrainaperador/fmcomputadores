fmcomputadores.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/', {
        templateUrl: 'templates/home.html',
        controller: 'homeCtrl'
      }).
      when('/product', {
        templateUrl: 'templates/product.html',
        controller: 'productCtrl'
      }).
      otherwise({
        redirectTo: '/'
      });
  }]);