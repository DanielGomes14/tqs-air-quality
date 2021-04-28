import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import { Card, CardContent,CardHeader,Typography } from '@material-ui/core';

const useStyles = makeStyles((theme) => ({
  root: {
        
    height: '100%',
    width: '100%'
      
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

export default function CacheCards(props) {
  const [spacing, setSpacing] = React.useState(10);
  const classes = useStyles();
  let titles = ["Number of Requests", "Number of Misses", "Number of Hits"]
  let content = [props.cachedata.numberRequests,props.cachedata.numberMisses,props.cachedata.numberHits]
  const handleChange = (event) => {
    setSpacing(Number(event.target.value));
  };

  return (
    <Grid container className={classes.root} spacing={2}>
      <Grid item md={12}>
        <Grid container justify="center" spacing={spacing}>
          {[0, 1, 2].map((value) => (
            <Grid key={value} item>
              <Card className={classes.root}>
                <CardHeader align='center' title={titles[value]}>
                </CardHeader>
                <CardContent>
                <Typography variant="h4" align = 'center' color="primary">
                  {content[value]}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Grid>
    </Grid>
  );
}
