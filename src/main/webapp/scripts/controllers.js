var cavControllers = angular.module("cavControllers", ['ngCookies','cavFactories']);
cavControllers.controller('indexController', function($http,$rootScope, $cookieStore, $cookies, $getCookiesAngular){
	$rootScope.title="Ágora@US";
	var getCookiesAngular = $getCookiesAngular();
	getCookiesAngular.then(function(result){
		$rootScope.username=$cookieStore.get("angularUser");
	});
	//DONDE PONCA PACOTE PONER $cookieStore.get("angularUser")
});
cavControllers.controller('createController', function($scope, $http, $rootScope, $location, $cookieStore){
	$rootScope.title="Crear votación";
	$scope.qs=[];
	$scope.i = 0;
	$scope.survey={type:"survey", usernameCreator:$cookieStore.get("angularUser"), title:"",description:"",startDate:"",endDate:"",questions:[], tipo:""};
	$scope.addQuestionField = function(){
		$scope.i+=1;
		$scope.qs.push($scope.i);
	};
	if($cookieStore.get("angularUser")!=null){
		$scope.submit = function(survey){
			console.log(survey);
			var data = null;
			
			$http.post("vote/save.do",survey).success(function(data,status){
				if (status=200){
					$http.get("/ADMCensus/census/create.do?idVotacion="+data.id+"&fecha_inicio="+data.startDate+"&fecha_fin="+data.endDate+"&tituloVotacion="+data.title+"&tipo"+data.tipo).success(function(data,status){
						if (status=200){
							$http.get("vote/saveCensus.do?surveyId="+data.idVotacion+"&censusId="+data.id).success(function(data,status){
								if (status=200){
									$location.path("/list");
								}
							});
						}
					});
				}
				$http.get("/Deliberations/customer/createThreadFromVotacion.do?name="+survey.title+"&censoId="+data.id);
			});
		};
	}
});
cavControllers.controller('listController', function($scope, $http, $route, $rootScope){
	$rootScope.title = "Lista de votaciones";
	$http.get("vote/mine.do").success(function(data,status){
		$scope.surveys = data;
	});
	
	$scope.borrar = function(survey){
		$http.get("/ADMCensus/census/canDelete.do?idVotacion="+survey.id).success(function(data,status){
			if (data[0].result=="yes"){
				$http.get("vote/delete.do?id="+survey.id).success(function(data,status){
					$route.reload();
				});
			}
		});
	};
	
	$scope.goEditCensus=function(survey){
		window.location="/ADMCensus/census/details.do?censusId="+survey.census;
	};
});