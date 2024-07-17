<%--
  Created by IntelliJ IDEA.
  User: 62499
  Date: 2024-07-10
  Time: 오후 2:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/init.jsp" %>
<liferay-ui:success key="entryAdded" message="entry-added" />
<liferay-ui:success key="entryUpdated" message="entry-updated" />
<liferay-ui:success key="entryDeleted" message="entry-deleted" />

<portlet:renderURL var="editEntryURL">
    <portlet:param name="action" value="editEntry" />
</portlet:renderURL>

<portlet:actionURL var="deleteEntryURL">
    <portlet:param name="action" value="deleteEntry" />
</portlet:actionURL>

<portlet:renderURL var="viewURL">
    <portlet:param name="action" value="view" />
</portlet:renderURL>

<script type="text/javascript" src="<c:url value='/js/modules/dropdown.js'/>"></script>

<ul class="nav nav-tabs">
    <c:forEach items="${guestbooks}" var="guestbook" varStatus="status">
        <li class="nav-item" data-guestbookId="${guestbook.guestbookId}">
            <a type="button" class="move-guestbook nav-link ${guestbookId eq guestbook.guestbookId ? 'active' : ''}">${guestbook.name}</a>
        </li>
    </c:forEach>
</ul>

<button type="button" id="edit-entry" class="btn btn-primary">Add Entry</button>

<table class="table mt-4">
    <thead>
    <tr>
        <th scope="col" class="text-center">Message</th>
        <th scope="col" class="text-center">Name</th>
        <th scrop="col" class="text-center">Action</th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${guestbookEntries.size() == 0}">
        <tr>
            <td colspan="3" class="text-center">No data was found.</td>
        </tr>
    </c:if>
    <c:forEach items="${guestbookEntries}" var="guestbookEntry" varStatus="status">
        <tr>
            <td class="text-center">${guestbookEntry.message}</td>
            <td class="text-center">${guestbookEntry.name}</td>
            <td class="">
                <%-- bootstrap의 css만 이용하고 기능을 Vanilla Javascript로만 구현한 예) dropdown.js 참고 --%>
                    <div class="text-center">
                        <div class="dropdown">
                            <button class="btn btn-secondary dropdown-toggle dropdown-toggle-ext" type="button"
                                    data-toggle="dropdown" aria-expanded="false">
                                Action
                            </button>
                            <div class="dropdown-menu" style="min-width: unset; left: 50%; transform: translateX(-50%)" data-entryId="${guestbookEntry.entryId}">
                                <button type="button" class="edit-entry dropdown-item">Edit</button>
                                <button type="button" class="delete-entry dropdown-item">Delete</button>
                            </div>
                        </div>
                    </div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>



<%--//TODO 페이징 필요, SearchContainer는 사용 안 함.--%>

<script>
    (async function () {
        /*
            Declare
        */
        const _namespace = '<portlet:namespace/>';
        const _url = {
            viewURL: '${viewURL}',
            editEntryURL: '${editEntryURL}',
            deleteEntryURL: '${deleteEntryURL}',
            fetchEntriesURL: '${fetchEntriesURL}',
        }

        const _guestbookId = Number('${guestbookId}');

        const moveGuestbookButtonEvent = (event) => {
            const guestbookId = event.target.parentNode.getAttribute("data-guestbookId");
            location.href = _url.viewURL + "&" + _namespace + "guestbookId=" + guestbookId;
        };


        const actionButtonEvent = (event) => {
            const entryId = event.target.parentNode.getAttribute("data-entryId");

            const buttonClass = event.target.classList;

            if (buttonClass.contains("edit-entry")) {
                performAction("editEntryURL", entryId);
            } else if (buttonClass.contains("delete-entry")) {
                performAction("deleteEntryURL", entryId);
            }
        };

        const performAction = (urlKey, entryId) => {
            const url = _url[urlKey];
            const queryParameters = [
                _namespace + "guestbookId=" + _guestbookId,
                _namespace + "entryId=" + entryId
            ].join("&");
            location.href = url + "&" + queryParameters;
        };

        const editEntry = () => {
            location.href = _url.editEntryURL + "&" + _namespace + "guestbookId=" + _guestbookId;
        }

        /*
            Event
        */

        const moveGuestbookButtons = document.querySelectorAll('.move-guestbook');
        moveGuestbookButtons.forEach((button) => {
            button.addEventListener('click', moveGuestbookButtonEvent);
        });

        document.getElementById('edit-entry').addEventListener('click', editEntry);

        const actionButtons = document.querySelectorAll('.edit-entry, .delete-entry');
        actionButtons.forEach((button) => {
            button.addEventListener('click', actionButtonEvent);
        });

        /*
            Initialization
        */
        const dropdown = new Dropdown('.dropdown-toggle');
        dropdown.init();
    }());
</script>
