import React from 'react';
import QualityResults from "../AirQualityResults/AirQualityResults"
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import SearchIcon from '@material-ui/icons/Search';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import IconButton from '@material-ui/core/IconButton';
import DialogTitle from '@material-ui/core/DialogTitle';
import OpenWeatherService from '../../services/OpenWeatherService'
const useStyles = makeStyles((theme) => ({
  table: {
    minWidth: 650,
  },
  form: {
    display: 'flex',
    flexDirection: 'column',
    margin: 'auto',
    width: 'fit-content',
  },
  formControl: {
    marginTop: theme.spacing(2),
    minWidth: 120,
  },
  formControlLabel: {
    marginTop: theme.spacing(1),
  },
}));

export default function CitiesTable(props) {
    let cities_arr = props.cities_list_data;
    const classes = useStyles();
    const [open, setOpen] = React.useState(null);
    const [ data, setData] = React.useState("");

    const handleClickOpen = () => {
      setOpen(true);
    };

    const handleClose = () => {
      setOpen(false);
    };

    let fetchData = (obj) => {
      OpenWeatherService.getForecastByLatAndLon(obj.row)
      .then( (res) => {
        if(res.status == 200)
          return res.json()
      })
      .then( ( res)=> {
        console.log(res)
        setData(res)
        console.log(data)
        setOpen(true);
      })
    }


    return (
    <div>
    {cities_arr.length > 0 ? 
    <div>
        <TableContainer component={Paper}>
            <Table className={classes.table} aria-label="simple table">
            <TableHead>
                <TableRow>
                <TableCell align="left">CityName</TableCell>
                <TableCell align="left">StateCode</TableCell>
                <TableCell align="left">Country</TableCell>
                <TableCell align="left">Coordinates</TableCell>
                <TableCell align="center"><p>Current and Forecast Air Quality</p></TableCell>      
            | </TableRow>
            </TableHead>
            <TableBody>
                {cities_arr.map((row) => (
                <TableRow align="left" key={row.cityid}>
                <TableCell align="left">{row.cityname}</TableCell>
                <TableCell align="left">{row.statecode}</TableCell>
                <TableCell align="left">{row.countyfull}({row.countrycode})</TableCell>
                <TableCell align="left"><p>{row.lat},{row.lon}</p></TableCell>
                <TableCell align="center">
                <IconButton color="primary" aria-label="search picture" onClick={ () => { fetchData({row});}} component="span">
                  <SearchIcon  fontSize='large'/>
                </IconButton>
                  
                </TableCell>
                </TableRow>
            ))}
            </TableBody>
        </Table>
        </TableContainer>
        <Dialog
        fullWidth={true}
        maxWidth={'md'}
        open={open}
        onClose={handleClose}
        aria-labelledby="max-width-dialog-title"
      >
        <DialogTitle id="max-width-dialog-title">Optional sizes</DialogTitle>
        <DialogContent>
          {data != null ? <QualityResults data={data}/>: "No results Found"}
          </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">Close
          </Button>
        </DialogActions>
      </Dialog>          
        </div>
        : ""
        }
    
    </div>
  );
}