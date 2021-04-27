import {API_BASE} from "config/urls";

class WeatherBitService{

    searchbyCity(searchquery){

        if(searchquery.includes(',')){
            let search_arr = searchquery.split(',')
            let [city_name,  country_code] = [search_arr[0], search_arr[1]]
            let url = 'current_quality';
            
            if (city_name != null && city_name.trim().length > 0) {
                url+='?cityname=' + city_name
            }
            console.log(country_code)
            if (country_code != null && country_code.trim().length > 0){
                alert("entrei")
                url+='&countrycode=' + country_code
            }
            alert(url)
            return fetch(API_BASE + url, {
                method: 'GET',
                mode: 'cors',
            }) 
        }
        else {
            alert("oof")
            return null
        }
        
    }
}

export default new WeatherBitService();