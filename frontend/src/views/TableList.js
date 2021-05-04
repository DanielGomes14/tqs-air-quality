import React from "react";
import CitiesService from "../services/CitiesService"
import CitiesTable from "../components/CitiesTable/CitiesTable"
import SearchBar from "../components/SearchBar/SearchBar"
import OpenWeatherService from "../services/OpenWeatherService"
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import Button from '@material-ui/core/Button';
import QualityResults from "../components/AirQualityResults/AirQualityResults"


// react-bootstrap components
import {
  Card,
  Container,
  Row,
  Col,
} from "react-bootstrap";

class TableList extends React.Component {
  constructor(props){
    super(props)
    this.state = {
      cities_list_data: null,
      open: false,

    }
    this.handleClickOpen = this.handleClickOpen.bind(this);
  }

  // kinda repeating but yeah..
  handleClickOpen = () => {
    this.state.open = true;
  };

  handleClose = () => {
    this.state.open = false;
  };

  handler = (coords) => {
    this.handleClickOpen()
    OpenWeatherService.getForecastByLatAndLon(coords)
    .then( (res) => {
      if(res.status == 200){
        return res.json();
      }
    }).then((res) => {
        this.setState({air_quality_data:res})
    })
    
  }
  componentDidMount() {
    CitiesService.getAllCities()
    .then( (res) => {
      if(res.status == 200)
        return res.json()
    })
    .then( ( res)=> {
      this.setState({cities_list_data: res})
    })
  }
  render(){
    return (
      <>
      <Container fluid>
        <Row>
          <Col md="12">
            <Card className="strpied-tabled-with-hover">
              <Card.Header>
                <Card.Title as="h4">Cities Details and Air Quality Forecast</Card.Title>
                <p className="card-category">
                  Check a list of some Cities and click on the button to check the forecast 
                  of Air Quality.
                </p>
              </Card.Header>
              <Card.Body className="table-full-width table-responsive px-0">
              <SearchBar label= "City Search by Coordinates" placeholder="Lat,Lon" handler = {this.handler} />
                {this.state.cities_list_data != null ? 
                <CitiesTable cities_list_data = {this.state.cities_list_data} />
                :
                null
              }
                
              </Card.Body>
            </Card>
            <Dialog
                fullWidth={true}
                maxWidth={'md'}
                open={this.state.open}
                onClose={this.handleClose}
                aria-labelledby="max-width-dialog-title">
                <DialogTitle id="max-width-dialog-title">Optional sizes</DialogTitle>
                <DialogContent>
                  {this.state.air_quality_data != null ?
                  <QualityResults data={this.state.air_quality_data}/> : ""}
                  </DialogContent>
                <DialogActions>
                  <Button onClick={this.handleClose} color="primary">Close
                  </Button>
                </DialogActions>
              </Dialog> 
          </Col>
        </Row>
      </Container>
    </>

    )
  }
}

export default TableList;
