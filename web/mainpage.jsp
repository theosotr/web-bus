<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
  <title>Application for Athens Urban Transport System</title>
  <meta http-equiv='content-type' content="text/html; charset=UTF-8"/>
  <link rel="stylesheet" href="css/pagestyle.css" type="text/css">
  <link rel="stylesheet" media="screen" href="css/bootstrap6.css" />
  <link rel="stylesheet" href="css/bootstrap.min.css">
  <link rel="stylesheet" type="text/css" media="screen" href="css/bootstrap-responsive.css"/>
  <link rel="stylesheet" href="css/jquery-ui.css"/>
  <link rel="stylesheet" href="css/jquery-ui.theme.css">
  <script>
    var pathConfigured = false;
    var map;
  </script>
  <script type="text/javascript" src="js/jquery.js"></script>
  <script src="js/bootstrap.min.js"></script>
  <script src="js/ie10-viewport-bug-workaround.js"></script>
  <script type="text/javascript" src="js/jquery-ui.js"></script>
  <script type="text/javascript" src="js/editCSSElements.js"></script>
  <script type="text/javascript" src="js/editHTMLElements.js"></script>
  <script type="text/javascript"
          src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXLRJtVzEOxAQOvzdGCapZFF-24zUTn_I&sensor=true">
  </script>
  <script type="text/javascript" src="js/maps.js"></script>
  <script type="text/javascript" src="js/stopHandling.js"></script>
  <script type="text/javascript" src="js/routeHandling.js"></script>
  <script type="text/javascript" src="js/guessTarget.js"></script>
  <script type="text/javascript" src="js/jQuery-ui-Initialization.js"></script>
  <script type="text/javascript">
    window.onload = hideRegistrationForm;
    function hideRegistrationForm() {
      map = new Map(37.979946, 23.727801);
      var date = new Date();
      $("#hour").val(date.getHours());
      $("#minute").val(date.getMinutes());
      $("#myonoffswitch").removeAttr("checked");
      $("#terminal").hide();
      $("#openDialog").hide();
      $("#routeNumber").hide();
      $("#name, #terminal, #routeNumber").val("");
    }
  </script>
</head>
<body id="a">
<nav class="navbar navbar-default" role="navigation">
  <div id = "menuBar" class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="brand" href="#">Καλώς ήρθατε στην εφαρμογή</a>
    </div>
    <div id="navbar" class="navbar-collapse collapse">
      <ul class="nav">
        <% if (session.getAttribute("user") != null) { %>
        <li id="stopChoice" class = "active"><a href="#stop" onclick="optimumPathMode(false);searchBasedOnStopLayout()">Αναζήτηση με βάση τη στάση</a></li>
        <li id="routeChoice"><a href="#route" onclick="optimumPathMode(false);searchBasedOnRouteLayout()">Αναζήτηση με βάση τη γραμμή</a></li>
        <li id="nolink">
          Εύρεση Βέλτιστης Διαδρομής
          <div class="onoffswitch">
            <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="myonoffswitch" onclick="optimumPathMode(true)">
            <label class="onoffswitch-label" for="myonoffswitch">
              <span class="onoffswitch-inner"></span>
              <span class="onoffswitch-switch"></span>
            </label>
            <% } else { %>
            <li id="stopChoice" class = "active"><a href="#stop" onclick="searchBasedOnStopLayout()">Αναζήτηση με βάση τη στάση</a></li>
            <li id="routeChoice"><a href="#route" onclick="searchBasedOnRouteLayout()">Αναζήτηση με βάση τη γραμμή</a></li>
            <li id="nolink">
              Εύρεση Βέλτιστης Διαδρομής
              <div class="onoffswitch">
                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="myonoffswitch" onclick="messageToLogin()">
                <label class="onoffswitch-label" for="myonoffswitch">
                  <span class="onoffswitch-inner"></span>
                  <span class="onoffswitch-switch"></span>
                </label>
            <% } %>
          </div>
        </li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <% if(session.getAttribute("user") != null) { %>
        <li id = "account" class="dropdown" style="margin-right: 40%;">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"><%= session.getAttribute("user")%><span class="caret"></span></a>
          <ul class="dropdown-menu dropdown-menu-right" role="menu">
            <li><a role="button" class='favorite' style='font-size: 11px;'><span class='ui-icon ui-icon-heart' style='float: left; margin-right: .3em;'></span>Αγαπημένες στάσεις</a></li>
            <li><a role="button" class='favouriteRoutes' style='font-size: 11px;'><span class='ui-icon ui-icon-heart' style='float: left; margin-right: .3em;'></span>Αγαπημένες γραμμές</a></li>
            <li><a href="/ismgroup26/logout" class='logout' style='font-size: 11px;'><span class='ui-icon ui-icon-power' style='float: left; margin-right: .3em;'></span>Αποσύνδεση</a></li>
          </ul>
        </li>
      <% } else { %>
          <a href="index.jsp" class="btn btn-primary" role="button">Συνδεθείτε</a>
      </ul>
      <% } %>
    </div><!--/.nav-collapse -->
  </div><!--/.container-fluid -->
</nav>
<div class="container-fluid">
  <h1 class="h1">Εφαρμογή για τις Αστικές Συγκοινωνίες Αθηνών</h1>
  <img src = "images/mmm.jpg" alt="Image not found">
</div>
<div id = "initialInfo" class="container-fluid">
  <p id="searchBar">
    <span id="inputDescription">Αναζήτηση στάσης</span> :&nbsp;
    <input id="routeNumber" type="text" name="routeNumber" placeholder="Πληκτρογείστε ονομασία γραμμής"/>
    <input class="stopSearch" placeholder="Πληκτρογείστε ονομασία στάσης" id = "name" type = "text"/>
    <input class="stopSearch" placeholder="Στάση Προορισμός" id = "terminal" type = "text"/>
    <button id="openDialog" style="font-size: 11px;"><span class="ui-icon ui-icon-newwin" style='float: left; margin-right: .3em;'></span>Παραμετροποίησε την διαδρομή</button>
  </p><br><br>
  <div id = "indiv">
    <div class="col-xs-4" style="width: 50%;"></div>
  </div>
</div><br><br><br><br><br><br><br><br><br>
<div id = "leftDivInfo"></div><br><br>
<div id = "map-canvas"></div><br><br>
<div id = "rightDivInfo"></div><br><br><br>
<div class="footer">
  <div id = "footer" class="container-fluid">
    <p id="leftfooter" class="text-muted"><i>Developped by Konstantinos Karakatsanis and Thodoris Sotiropoulos</i></p>
    <p id="rightfooter" class="text-muted">&copy; 2014-2015 Τμήμα Διοικητικής Επιστήμης και Τεχνολογίας - Οικονομικό Πανεπιστήμο Αθηνών</p>
  </div>
</div>
<%if (session.getAttribute("user") != null) { %>
<jsp:include page="favouriteStopsDialog.jsp"></jsp:include>
<jsp:include page="favouriteRoutesDialog.jsp"></jsp:include>
<jsp:include page="optimalPathDialog.jsp"></jsp:include>
<jsp:include page="messageDialog.jsp"></jsp:include>
<% } else { %>
<jsp:include page="messageDialog.jsp"></jsp:include>
<% } %>
</body>
</html>