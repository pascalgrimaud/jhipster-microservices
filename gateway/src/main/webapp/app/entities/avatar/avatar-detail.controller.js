(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('AvatarDetailController', AvatarDetailController);

    AvatarDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Avatar'];

    function AvatarDetailController($scope, $rootScope, $stateParams, previousState, entity, Avatar) {
        var vm = this;

        vm.avatar = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:avatarUpdate', function(event, result) {
            vm.avatar = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
