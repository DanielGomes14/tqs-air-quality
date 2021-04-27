import {API_BASE} from "config/urls";


class CacheService{

    searchCacheStats(){
        let url = 'cache-statistics'
        return fetch(API_BASE + url,{
            method: 'GET',
            mode: 'cors',
        } )
    }
}

export default new CacheService();