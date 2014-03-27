/** @jsx React.DOM */

var React = require("react");
var MoodlyBackend = require("./backend");
var Start = require("./start")(MoodlyBackend);

React.renderComponent(<Start />, document.getElementById("moodly-admin"));
