import {API_BASE} from "config/urls";

class WeatherBitService{

    searchbyCity(searchquery){
        let url = 'current_quality';
        if(searchquery.includes(',')){
            let search_arr = searchquery.split(',')
            let [city_name,  country_code] = [search_arr[0], search_arr[1]]
            
            
            if (city_name != null && city_name.trim().length > 0) {
                url+='?cityname=' + city_name
            }
            if (country_code != null && country_code.trim().length > 0){
                url+='&countrycode=' + country_code
            }
        }
        return fetch(API_BASE + url, {
            method: 'GET',
            mode: 'cors',
        })
        
    }

    getForecastByCityAndCountry(searchquery) {
        let [city_name,  country_code] = [null,null]
        let url = 'forecast';
        if(typeof searchquery == 'string'){
            if(searchquery.includes(',')){
                let search_arr = searchquery.split(',');
                city_name = search_arr[0];
                country_code = search_arr[1];
            }
        }
        else{
            city_name = searchquery.cityname
            country_code = searchquery.countrycode
        }
        if(city_name != null && country_code != null){
            url+='?cityname=' + city_name
            url+='&countrycode=' + country_code
        } 
        return fetch(API_BASE + url, {
            method: 'GET',
            mode: 'cors',
        })
    }
}

export default new WeatherBitService();