<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Application for Athens Urban Transport System</title>
  <meta http-equiv='content-type' content="text/html; charset=UTF-8"/>
  <link rel="stylesheet" href="css/pagestyle.css" type="text/css">
  <link rel="stylesheet" href="css/bootstrap.min.css">
  <link rel="stylesheet" type="text/css" media="screen" href="css/bootstrap-responsive.css"/>
  <script type="text/javascript" src="js/jquery.js"></script>
  <script type="text/javascript" src="js/editHTMLElements.js"></script>
  <script type="text/javascript" src = "js/registrationAdmin.js"></script>
  <script type="text/javascript"
          src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXLRJtVzEOxAQOvzdGCapZFF-24zUTn_I&sensor=true">
  </script>
  <script type = "text/javascript">
    window.onload = hideRegistrationForm;
    function hideRegistrationForm() {
      $('#registration').hide();
    }
  </script>
  <script type="text/javascript">
    /**
     * function that initializes map
     */
    function initialize() {
      var myLatlng = new google.maps.LatLng(37.979946, 23.727801);
      var mapOptions = {
        zoom: 13,
        center: myLatlng
      }
      var map = new google.maps.Map(document.getElementById('map-display'), mapOptions);
      var transitLayer =  new google.maps.TransitLayer();
      transitLayer.setMap(map);
    }

    google.maps.event.addDomListener(window, 'load', initialize);
  </script>
</head>
<body id="a">
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
  <div id = "menuBar" class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a id="welcome" class="navbar-brand" href="mainpage.jsp">Καλώς ήρθατε στην εφαρμογή</a>
    </div>
    <div id="navbar" class="navbar-collapse collapse">
      <form method="post" action="/ismgroup26/CheckLoginServlet" class="navbar-form navbar-right" role="form">
        <div class="form-group">
          <input type="text" placeholder="Username" class="form-control" name="username">
        </div>
        <div class="form-group">
          <input type="password" placeholder="Κωδικός Πρόσβασης" class="form-control" name="password">
        </div>
        <input type="hidden" value="index" name="source">
        <button type="submit" class="btn btn-primary">Sign in</button>
        <% if(session.getAttribute("errorMessage") != null) { %>
          <div style="color: #ffb90a; text-align: center;"><%= session.getAttribute("errorMessage")%></div>
        <% session.removeAttribute("errorMessage");
          } %>
      </form>
    </div><!--/.navbar-collapse -->
  </div>
</nav>
<div id = "appDesc">
  <div class="panel panel-warning">
    <div class="panel-body">
      <div id = "description" class="jumbotron">
        <h1 class = "h1">Εφαρμογή για τα λεωφορεία του ΟΑΣΑ</h1>
        <div id = "map-display">
        </div>
        <p id = "message" class="lead">Ο καλύτερος οδηγός για τις μετακινήσεις σας στην Αθήνα. Παρέχει πλούσιες δυνατότητες, ώστε
          να έχετε έγκυρη και έγκαιρη πληροφορόρηση για τα δρομολόγια του ΟΑΣΑ, για να προγραμματίσετε τις διαδρομές σας εύκολα και
          χωρίς κόπο.</p>
        <div class="alert alert-info" role="alert">
          Αν δεν είστε χρήστης μπορείτε ευκόλα μέσα σε λίγα λεπτά να χρησιμοποιήσετε την εφαρμογή
        </div>
        <p class = "message"><button onclick = "editRegistrationForm()" type="submit" class="btn btn-primary">Εγγραφείτε τώρα</button></p>
      </div>
    </div>
  </div>
</div>
<div id = "registration">
  <h4>Εγγραφή νέου χρήστη</h4>
  <div id = "registrationForm">
    <form>
    <b>Όνομα</b><span class = "asterisc">*</span>
    <input id = "firstName" type="text" name = "firstName" class="form-control" placeholder="Δώστε το όνομά σας"><br>
    <b>Επώνυμο</b><span class = "asterisc">*</span>
    <input id = "surname" type="text" name = "surname" class="form-control" placeholder="Δώστε το επίθετό σας"><br>
    <b>Username</b><span class = "asterisc">*</span><span id = "acceptusername" class = "checkInputs"></span>
    <input id = "username" name = "username" onchange = "checkInputIfExists('username')" type="text" class="form-control" placeholder="Δώστε το username σας"><br>
    <b>e-mail</b><span class = "asterisc">*</span><span id = "acceptemail" class = "checkInputs"></span>
    <div class="input-group">
      <div class="input-group-addon">@</div>
      <input id = "email" class="form-control" name = "email" onchange = "checkInputIfExists('email')" type="email" placeholder="Δώστε το e-mail σας">
    </div><br>
    <b>Κωδικός Πρόσβασης</b><span class = "asterisc">*</span>
    <input id = "pswrd1" type="password" name = "password" class="form-control" placeholder="Δώστε τον κωδικό πρόσβασής σας"><br>
    <b>Κωδικός Πρόσβασης</b><span class = "asterisc">*</span>
    <input id = "pswrd2" type="password" name = "password2" class="form-control" onchange = "verifyPasswords()" placeholder="Επιβεβαιώστε τον κωδικό πρόσβασής σας"><br>
    <span id = "acceptPswrd" class = "checkInputs"></span><br><br>
    <span class = "asterisc">*</span>Πεδία υποχρεωτικά<br><br>
    <button onclick = "createAccount()" type="button" class="btn btn-primary">Υποβολή</button>
    <button type="reset" class="btn btn-primary">Άκυρο</button>
    </form>
  </div>
</div><br><br><br>
<div id = "signature" class="footer">
  <div id = "footer" class="container-fluid">
    <p id="leftfooter" class="text-muted"><i>Developed by Konstantinos Karakatsanis, Theodoros Sotiropoulos and Georgios Foustanos</i></p>
    <p id="rightfooter" class="text-muted">&copy; 2014-2015 Τμήμα Διοικητικής Επιστήμης και Τεχνολογίας - Οικονομικό Πανεπιστήμο Αθηνών</p>
  </div>
</div>
<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>