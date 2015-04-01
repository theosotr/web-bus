<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="loginDialog" title="Είσοδος στο σύστημα">
  <p>Για να χρησιμοποίησετε την αναζήτηση βέλτιστης διαδρομής θα πρέπει να εισέλθετε στο σύστημα.</p>
  <form id="login" method="post" action="CheckLoginServlet" class="navbar-form navbar-right" role="form">
    <div class="form-group">
      <label for="username">Όνομα χρήστη: </label><br>
      <input id="username" type="text" placeholder="Username" class="form-control" name="username" style="width:80%; height: 35px;">
    </div><br>
    <div class="form-group">
      <label for="password">Κωδικός πρόσβασης: </label><br>
      <input id="password" type="password" placeholder="Κωδικός Πρόσβασης" class="form-control" name="password" style="width:80%;height: 35px;">
    </div><br><br>
    <input type="hidden" value="mainpage" name="source">
    <a href="index.jsp"><span class="asterisc" style="font-size: 12px;">Έαν δεν έχετε λογαριασμό στο σύστημα, πατήστε εδώ να εγγραφείτε</span></a><br>
    <% if(session.getAttribute("errorMessage") != null) { %>
    <div style="color: #ffb90a; text-align: center;"><%= session.getAttribute("errorMessage")%></div>
    <% session.removeAttribute("errorMessage");
    } %>
  </form>
</div>
