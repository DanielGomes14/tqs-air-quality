/*!

=========================================================
* Light Bootstrap Dashboard React - v2.0.0
=========================================================

* Product Page: https://www.creative-tim.com/product/light-bootstrap-dashboard-react
* Copyright 2020 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/light-bootstrap-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import Dashboard from "views/Dashboard.js";
import TableList from "views/TableList.js";
import Cache from "views/Cache.js";

const dashboardRoutes = [
  {
    path: "/dashboard",
    name: "Search By City",
    icon: "nc-icon nc-zoom-split",
    component: Dashboard,
    layout: "/admin",
  },
  {
    path: "/citieslist",
    name: "Cities List and Forecast",
    icon: "nc-icon nc-notes",
    component: TableList,
    layout: "/admin",
  },
  {
    path: "/cache",
    name: "Cache Stats",
    icon: "nc-icon nc-atom",
    component: Cache,
    layout: "/admin",
  }
];

export default dashboardRoutes;
