<%--
  Created by IntelliJ IDEA.
  User: 62499
  Date: 2024-07-12
  Time: 오전 9:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/init.jsp" %>

<portlet:renderURL var="viewURL">
    <portlet:param name="action" value="view" />
</portlet:renderURL>

<portlet:actionURL var="saveEntryURL">
    <portlet:param name="action" value="saveEntry" />
</portlet:actionURL>

<form id="form" name="<portlet:namespace/>form">
    <input type="hidden" id="guestbookId" name="<portlet:namespace/>guestbookId" class="form-control" value="${entry eq null ? guestbookId : entry.getGuestbookId()}">
    <input type="hidden" id="entryId" name="<portlet:namespace/>entryId" class="form-control" value="${entry.getEntryId()}">
    <div class="form-group">
        <label for="name">Name</label>
        <input type="text" id="name" name="<portlet:namespace/>name" class="form-control" value="${entry.getName()}">
    </div>
    <div class="form-group">
        <label for="email">Email</label>
        <input type="text" id="email" name="<portlet:namespace/>email" class="form-control" value="${entry.getEmail()}">
    </div>
    <div class="form-group">
        <label for="message">Message</label>
        <input type="text" id="message" name="<portlet:namespace/>message" class="form-control" value="${entry.getMessage()}">
    </div>

    <button type="button" id="form-submit" class="btn btn-primary">Submit</button>
    <button type="button" id="form-cancel" class="btn btn-default">Cancel</button>
</form>

<script>
    (async function () {
        /**
         * Declare
         */

        const _url = {
            baseURL: '${pageContext.request.contextPath}',
            viewURL: '${viewURL}',
            saveEntryURL: '${saveEntryURL}'
        }

        const submitForm = () => {
            const form = document.getElementById('form');
            form.method = 'POST';
            form.action = _url.saveEntryURL;
            form.submit();
        }

        const cancelForm = () => {
            location.href = '${viewURL}';
        }

        /*
            Event
         */
        const submitButton = document.getElementById('form-submit');
        submitButton.addEventListener('click', submitForm);

        const cancleButton = document.getElementById('form-cancel');
        cancleButton.addEventListener('click', cancelForm);
    }());


</script>














