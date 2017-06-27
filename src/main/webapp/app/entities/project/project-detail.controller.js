(function() {
    'use strict';

    angular
        .module('projectoneApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Project', 'Issue', 'User'];

    function ProjectDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Project, Issue, User) {
        var vm = this;

        vm.myProjects = Project.myprojects();
        vm.project = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('projectoneApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
