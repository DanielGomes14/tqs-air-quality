import React from "react";
import CitiesService from "../services/CitiesService"
import CitiesTable from "../components/CitiesTable/CitiesTable"
import SearchBar from "../components/SearchBar/SearchBar"
import WeatherBitService from "../services/WeatherBitService"
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import Button from '@material-ui/core/Button';
import QualityResults from "../components/AirQualityResults/AirQualityResults"
import Typography from '@material-ui/core/Typography';


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
      has_error:true,
      error_message:""
    }
  }

  // kinda repeating but yeah..
  handleClickOpen = () => {
    this.setState({open:true});

  };

  handleClose = () => {
    this.setState({open:false});
  };

  handler = (inputquery) => {
    this.handleClickOpen()
    WeatherBitService.getForecastByCityAndCountry(inputquery)
    .then( (res) => {
      if(res.status == 200){
        this.setState({has_error:false, error_message:""})
        return res.json();
      }
      this.setState({has_error:true, error_message: "City Or Country Not Found!"})
    }).then((res) => {
        this.setState({air_quality_data:res})
    })
    .catch( () => {
      this.setState({has_error:true, error_message:"Some Error Occured"})
    });
    
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
              <SearchBar label= "Forecast Air Quality Data Search" placeholder="Aveiro,PT" handler = {this.handler} />
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
                <DialogTitle id="max-width-dialog-title">Air Quality Results</DialogTitle>
                <DialogContent>
                  {this.state.air_quality_data != null ?
                  <QualityResults data={this.state.air_quality_data}/> : (this.state.has_error)?
                  <Typography variant="body1" align = 'center' color="error">
                  {this.state.error_message}
                  </Typography>
                  : ""}
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
