(function() {
    'use strict';

    angular
        .module('projectoneApp')
        .factory('RequirementSearch', RequirementSearch);

    RequirementSearch.$inject = ['$resource'];

    function RequirementSearch($resource) {
        var resourceUrl =  'api/_search/requirements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
