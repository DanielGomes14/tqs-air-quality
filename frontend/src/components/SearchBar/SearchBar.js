import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button'
import { makeStyles } from '@material-ui/core/styles';
import React from 'react';
import { Col, Row } from 'react-bootstrap';

const useStyles = makeStyles((theme) => ({
    root: {
      display: 'flex',
      flexWrap: 'wrap',
    },
    multilineColor:{
      color:'aqua'
    },
    textField: {
      marginLeft: theme.spacing(1),
      marginRight: theme.spacing(1),
      "& .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
        borderColor: "#35baf6"
      },
      "& .MuiOutlinedInput-input": {
        color: "#35baf6"
      },
      "& .MuiInputLabel-outlined": {
        color: "#72cff8"
      }
    }
  }));
function SearchBar(props) {
    const classes = useStyles();
    return (
      <div>
        <Row>
        <Col md="10">
        <TextField
        fullWidth = {true}
        label="City Search"
        id="outlined-margin-normal"
        placeholder="Aveiro,PT"
        className={classes.textField}
        InputProps={{
          className: classes.multilineColor
        }}
        margin="normal"
        variant="outlined"
        size = "medium"
        />
        </Col>
        <Col md="2">
        <Button variant="outlined" color="primary" onClick = {()=> props.handler(
          document.getElementById('outlined-margin-normal').value
        )}
        href="#outlined-buttons">
          Search
        </Button>
        </Col>
        </Row>
        </div>
    )
}
export default SearchBar;
