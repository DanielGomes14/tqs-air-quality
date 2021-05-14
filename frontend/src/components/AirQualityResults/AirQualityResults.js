import Typography from "@material-ui/core/Typography";
import { makeStyles } from '@material-ui/core/styles';
import React from 'react';
import {Card,CardContent, CardHeader,} from "@material-ui/core"
import {Row,Col} from "react-bootstrap"
const useStyles = makeStyles((theme) => ({
    root: {
        
        height: '100%'
        
    },
    media: {
        height: 0,
        paddingTop: '56.25%', // 16:9
    },
    expand: {
        transform: 'rotate(0deg)',
        marginLeft: 'auto',
        transition: theme.transitions.create('transform', {
        duration: theme.transitions.duration.shortest,
        }),
    },
    expandOpen: {
        transform: 'rotate(180deg)',
    },
}));
    
    const getDays = (numdays) => {
        let arr = [];
        let today = new Date();
        for(let i = 0; i< numdays; i++){
            let tomorrow = new Date();
            tomorrow.setDate(today.getDate() + i);
            let parsed = tomorrow.getDate() +
                        "-" + (tomorrow.getMonth() + 1) + "-"+ tomorrow.getFullYear()
            arr.push(parsed);            
        }
        return arr;
    }

function QualityResults(props){
    const city_name =  props.data.city_name
    const classes = useStyles();
    let arr_days = getDays(props.data.data.length)
    const [expanded, setExpanded] = React.useState(false);

    const handleExpandClick = () => {
        setExpanded(!expanded);
    };
        


    return (
        <>
        <Typography
        className={"MuiTypography--heading"}
        variant={"h4"}
        color= "primary"
        align = 'center'>
        {city_name},{props.data.country_code}
        </Typography>
        <Typography
            variant={"overline"}
            color= "primary"
            display="block"
            align = 'center'
        >
        Location: {props.data.lat},{props.data.lon}
        </Typography>
        
        {props.data.data.map((it,index) => (
            <div>
            <Typography
                variant={"overline"}
                color= "primary"
                gutterBottom
                display="block"
                align = 'center'
            >
            Air Quality Index: {it.aqi}
        </Typography>
                <Row>
                    <Col md="12">
                    <Typography variant="h6" gutterBottom display="block" align = 'center' color="primary">
                    Air Quality for {arr_days[index]}</Typography>
                    </Col>
                </Row>
            <Row >  
                <Col md="4">
                <Card className={classes.root}>
                <CardHeader
                title="Ozone (o3)"
                align='center'
                />
                <CardContent>
                <Typography variant="h4" align = 'center' color="primary">
                    {it.o3}
                </Typography>                
                </CardContent>
                </Card>
                </Col>
                <Col md="4">
                <Card className={classes.root}>
                    <CardHeader
                    title="Carbon Monoxide (co)"
                    align='center'
                    />
                    <CardContent>
                        <Typography variant="h4" align = 'center' color="primary">
                            {it.co}
                        </Typography>
                    </CardContent>
                    </Card>
                </Col>   
                <Col md="4">
                    <Card className={classes.root}>
                        <CardHeader
                        title="Sulfur Dioxide (so2)"
                        align='center'
                        />
                        <CardContent>
                        <Typography variant="h4" align = 'center' color="primary">
                            {it.so2}
                        </Typography>
                        </CardContent>
                    </Card>
                </Col>
                </Row>
                <br/>

                <br/>
                <Row >
                    <Col md="4">
                        <Card className={classes.root}>
                            <CardHeader
                            title="Nitrogren Dioxide (no2)"
                            align='center'
                            />
                            <CardContent>
                            <Typography variant="h4" align = 'center' color="primary">
                                {it.no2}
                            </Typography>
                            </CardContent>
                        </Card>
                    </Col>
                    <Col md="4">
                        <Card className={classes.root}>
                            <CardHeader
                            title="Inh. Particl. Manner (pm10)"
                            align='center'
                            />
                            <CardContent>
                            <Typography variant="h4" align = 'center' color="primary">
                                {it.pm10}
                            </Typography>
                            </CardContent>
                        </Card>
                    </Col>
                    <Col md="4">
                        <Card className={classes.root}>
                            <CardHeader
                            title="Fine Particl. Manner (pm25)"
                            align='center'
                            />
                            <CardContent>
                            <Typography variant="h4" align = 'center' color="primary">
                                {it.pm25}
                            </Typography>
                            </CardContent>
                        </Card>
                    </Col>
            </Row>
            <br/>
            <br/>
            <br/>
        </div>
        ))}
        
        </>
    )
}
export default QualityResults;