import {API_BASE} from "config/urls";
class CitiesService {
    getAllCities() {
        let url = 'cities'
        return fetch(API_BASE +url, {
            method: 'GET',
            mode: 'cors',
        })
    }
}
export default new CitiesService();