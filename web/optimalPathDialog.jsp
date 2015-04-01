<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="dialog" title="Παραμετροποίηση διαδρομής">
  <form>
    <fieldset>
      <div id="radio">
        <input type="radio" id="radio1" name="radio" checked="checked" value="false"><label for="radio1" style="font-size: 11px;">Αναζήτησε με βάση την ώρα αναχώρησης</label>
        <input type="radio" id="radio2" name="radio" value="true"><label for="radio2" style="font-size: 11px;">Αναζήτησε με βάση την ώρα άφιξης</label>
      </div>
      <p>
        <label for="hour">Επιλογή ώρας: </label>
        <input id="hour" name="hour">
      </p>
      <p>
        <label for="minute">Επιλογή λεπτών: </label>
        <input id="minute" name="minute">
      </p>
      <label for="transport">Επιλογή μέσου μεταφοράς</label>
      <select id="transport" name = "meanOfTransport">
        <option value="TRANSIT">ΜΜΜ</option>
        <option value="DRIVING">Αυτοκίνητο</option>
        <option value="WALKING">Περπάτημα</option>
      </select>
    </fieldset>
  </form>
</div>