/**
 * UPMSocialReadingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
package es.upm.fi.sos;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import es.upm.fi.sos.UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordResponseE;
import es.upm.fi.sos.UPMAuthenticationAuthorizationWSSkeletonStub.*;
import es.upm.fi.sos.xsd.*;

/**
 * UPMSocialReadingSkeleton java skeleton for the axisService
 */
public class UPMSocialReadingSkeleton {
	private static UPMAuthenticationAuthorizationWSSkeletonStub stub = null;
	private static HashMap<String, String> users;
	private static HashMap<String, ArrayList<String>> friends;
	private static HashMap<String, ArrayList<Book>> readings;

	private int sessions;
	private User user;

	public UPMSocialReadingSkeleton() throws Exception {
		if (stub == null) {
			stub = new UPMAuthenticationAuthorizationWSSkeletonStub();
			stub._getServiceClient().engageModule("addressing");
			stub._getServiceClient().getOptions().setManageSession(true);

			users = new HashMap<>();
			readings = new HashMap<>();
			friends = new HashMap<>();

			users.put("admin", "admin");
			// crear una lista vacio de amigos y de sus lecturas solo para admin
			friends.put("admin", new ArrayList<>());
			readings.put("admin", new ArrayList<>());
		}
		this.sessions = 0;
	}

	/**
	 * Auto generated method signatureS
	 * 
	 * @param logout
	 * @return
	 */

	public void logout(Logout logout) {
		if (user == null) {
			return;
		}

		this.sessions--;
		if (this.sessions == 0) {
			this.user = null;
		}
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param addFriend
	 * @return addFriendResponse
	 * @throws RemoteException
	 */

	public AddFriendResponse addFriend(AddFriend addFriend)
			throws RemoteException {
		Response response = new Response();
		response.setResponse(false);
		AddFriendResponse addFriendResponse = new AddFriendResponse();
		addFriendResponse.set_return(response);

		// si se ha logeado anteriormente
		if (this.user == null) {
			return addFriendResponse;
		}
		
		// si fue borrado el usuario por otra sesion
		if (users.get(this.user.getName()) == null) {
			return addFriendResponse;
		}

		// comprobar la existencia del usuario como amigo en la red social
		UPMAuthenticationAuthorizationWSSkeletonStub.Username username = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
		username.setName(addFriend.getArgs0().getUsername());
		ExistUser existUser = new ExistUser();
		existUser.setUsername(username);
		ExistUserResponseE existResponse = stub.existUser(existUser);
		if (!existResponse.get_return().getResult()) {
			return addFriendResponse;
		}

		// si ya fue aniadido como amigo
		for (String friend : friends.get(this.user.getName())) {
			if (friend.equals(addFriend.getArgs0().getUsername())) {
				return addFriendResponse;
			}
		}

		// aniadir como amigo
		friends.get(this.user.getName())
				.add(addFriend.getArgs0().getUsername());
		response.setResponse(true);

		return addFriendResponse;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param removeFriend
	 * @return removeFriendResponse
	 * @throws RemoteException
	 */

	public RemoveFriendResponse removeFriend(RemoveFriend removeFriend)
			throws RemoteException {
		Response response = new Response();
		response.setResponse(false);
		RemoveFriendResponse removeFriendResponse = new RemoveFriendResponse();
		removeFriendResponse.set_return(response);

		// si no habia logeado anteriormente
		if (this.user == null) {
			return removeFriendResponse;
		}
		
		// si fue borrado el usuario por otra sesion
		if (users.get(this.user.getName()) == null) {
			return removeFriendResponse;
		}

		// comprobar la existencia del usuario como amigo en la red social
		UPMAuthenticationAuthorizationWSSkeletonStub.Username username = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
		username.setName(removeFriend.getArgs0().getUsername());
		ExistUser existUser = new ExistUser();
		existUser.setUsername(username);
		ExistUserResponseE existResponse = stub.existUser(existUser);
		if (!existResponse.get_return().getResult()) {
			return removeFriendResponse;
		}

		// se comprueba si existe ese amigo
		String userFriend = removeFriend.getArgs0().getUsername();
		ArrayList<String> myFriends = friends.get(this.user.getName());
		for (int i = 0; i < myFriends.size(); i++) {
			if (myFriends.get(i).equals(userFriend)) {
				myFriends.remove(i);
				response.setResponse(true);
				return removeFriendResponse;
			}
		}

		return removeFriendResponse;

	}

	/**
	 * Auto generated method signature
	 * 
	 * @param getMyFriendReadings
	 * @return getMyFriendReadingsResponse
	 * @throws RemoteException
	 */

	public GetMyFriendReadingsResponse getMyFriendReadings(
			GetMyFriendReadings getMyFriendReadings) throws RemoteException {
		TitleList titleList = new TitleList();
		titleList.setResult(false);
		titleList.setTitles(new String[0]);
		GetMyFriendReadingsResponse getMyFriendReadingsResponse = new GetMyFriendReadingsResponse();
		getMyFriendReadingsResponse.set_return(titleList);

		// si no habia logeado anteriormente
		if (this.user == null) {
			return getMyFriendReadingsResponse;
		}
		
		// si fue borrado el usuario por otra sesion
		if (users.get(this.user.getName()) == null) {
			return getMyFriendReadingsResponse;
		}

		// comprobar la existencia del usuario como amigo en la red social
		UPMAuthenticationAuthorizationWSSkeletonStub.Username username = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
		username.setName(getMyFriendReadings.getArgs0().getUsername());
		ExistUser existUser = new ExistUser();
		existUser.setUsername(username);
		ExistUserResponseE existResponse = stub.existUser(existUser);
		if (!existResponse.get_return().getResult()) {
			return getMyFriendReadingsResponse;
		}

		// comprobar si es amigo del usuario de las lecturas
		if (!friends.containsKey(getMyFriendReadings.getArgs0().getUsername())) {
			return getMyFriendReadingsResponse;
		}

		boolean isFriend = false;
		for (String friend : friends.get(getMyFriendReadings.getArgs0()
				.getUsername())) {
			if (friend.equals(this.user.getName())) {
				isFriend = true;
				break;
			}
		}

		if (!isFriend) {
			return getMyFriendReadingsResponse;
		}

		// comprobar la existencia de las lecturas del usuario a consultar
		if (!readings.containsKey(getMyFriendReadings.getArgs0().getUsername())) {
			titleList.setResult(true);
			return getMyFriendReadingsResponse;
		}

		// crear un array inversa de los titulos de libros
		ArrayList<Book> friendReadings = readings.get(getMyFriendReadings
				.getArgs0().getUsername());
		String[] titles = new String[friendReadings.size()];

		for (int i = 0; i < friendReadings.size(); i++) {
			titles[i] = friendReadings.get(friendReadings.size() - 1 - i)
					.getTitle();
		}

		titleList.setResult(true);
		titleList.setTitles(titles);

		return getMyFriendReadingsResponse;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param getMyFriendList
	 * @return getMyFriendListResponse
	 */

	public GetMyFriendListResponse getMyFriendList(
			GetMyFriendList getMyFriendList) {
		FriendList friendList = new FriendList();
		friendList.setResult(false);
		friendList.setFriends(new String[0]);
		GetMyFriendListResponse getMyFriendListResponse = new GetMyFriendListResponse();
		getMyFriendListResponse.set_return(friendList);

		// si no habia logeado anteriormente
		if (this.user == null) {
			return getMyFriendListResponse;
		}
		
		// si fue borrado el usuario por otra sesion
		if (users.get(this.user.getName()) == null) {
			return getMyFriendListResponse;
		}

		// crear un array de amigos
		ArrayList<String> myFriends = friends.get(this.user.getName());
		String[] friends = new String[myFriends.size()];

		friendList.setFriends(myFriends.toArray(friends));
		friendList.setResult(true);
		return getMyFriendListResponse;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param addUser
	 * @return addUserResponse
	 * @throws RemoteException
	 */

	public AddUserResponse addUser(AddUser addUser) throws RemoteException {
		es.upm.fi.sos.xsd.AddUserResponse addUserResponseXsd = new es.upm.fi.sos.xsd.AddUserResponse();
		addUserResponseXsd.setResponse(false);
		es.upm.fi.sos.AddUserResponse addUserResponse = new es.upm.fi.sos.AddUserResponse();
		addUserResponse.set_return(addUserResponseXsd);

		if (user == null) {
			return addUserResponse;
		}

		if (!user.getName().equals("admin")) {
			return addUserResponse;
		}

		UserBackEnd userBackEnd = new UserBackEnd();
		userBackEnd.setName(addUser.getArgs0().getUsername());
		UPMAuthenticationAuthorizationWSSkeletonStub.AddUser addUserBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.AddUser();
		addUserBackEnd.setUser(userBackEnd);
		UPMAuthenticationAuthorizationWSSkeletonStub.AddUserResponse addUserResponseBackEnd = stub
				.addUser(addUserBackEnd);
		if (addUserResponseBackEnd.get_return().getResult()) {
			addUserResponseXsd.setResponse(true);
			addUserResponseXsd.setPwd(addUserResponseBackEnd.get_return()
					.getPassword());
		}

		return addUserResponse;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param removeUser
	 * @return removeUserResponse
	 * @throws RemoteException
	 */

	public RemoveUserResponse removeUser(RemoveUser removeUser)
			throws RemoteException {
		Response response = new Response();
		response.setResponse(false);
		RemoveUserResponse removeUserResponse = new RemoveUserResponse();
		removeUserResponse.set_return(response);

		// si no habia logeado anteriormente
		if (this.user == null) {
			return removeUserResponse;
		}
		
		// si fue borrado el usuario por otra sesion
		if (users.get(this.user.getName()) == null) {
			return removeUserResponse;
		}

		// si el usuario que quiere borrar es admin
		if (removeUser.getArgs0().getUsername().equals("admin")) {
			return removeUserResponse;
		}

		// si no es admin ni coinciden el nombre de usuario que quiere borrar
		if (!this.user.getName().equals("admin")
				&& !this.user.getName().equals(
						removeUser.getArgs0().getUsername())) {
			return removeUserResponse;
		}

		UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUser removeUserBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUser();
		removeUserBackEnd.setName(removeUser.getArgs0().getUsername());
		removeUserBackEnd.setPassword(users.get(removeUser.getArgs0()
				.getUsername()));
		RemoveUserE removeUserE = new RemoveUserE();
		removeUserE.setRemoveUser(removeUserBackEnd);
		RemoveUserResponseE removeUserResponseE = stub.removeUser(removeUserE);
		if (removeUserResponseE.get_return().getResult()) {
			response.setResponse(true);
			users.remove(removeUser.getArgs0().getUsername());
			friends.remove(removeUser.getArgs0().getUsername());
			readings.remove(removeUser.getArgs0().getUsername());
		}
		return removeUserResponse;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param getMyReadings
	 * @return getMyReadingsResponse
	 */

	public GetMyReadingsResponse getMyReadings(GetMyReadings getMyReadings) {
		TitleList titleList = new TitleList();
		titleList.setResult(false);
		titleList.setTitles(new String[0]);
		GetMyReadingsResponse getMyReadingsResponse = new GetMyReadingsResponse();
		getMyReadingsResponse.set_return(titleList);

		// si no habia logeado anteriormente
		if (this.user == null) {
			return getMyReadingsResponse;
		}

		// si fue borrado el usuario por otra sesion
		if (users.get(this.user.getName()) == null) {
			return getMyReadingsResponse;
		}

		// crear un array inversa de los titulos de libros
		ArrayList<Book> myReadings = readings.get(this.user.getName());
		String[] titles = new String[myReadings.size()];

		for (int i = 0; i < myReadings.size(); i++) {
			titles[i] = myReadings.get(myReadings.size() - 1 - i).getTitle();
		}

		titleList.setResult(true);
		titleList.setTitles(titles);
		return getMyReadingsResponse;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param addReading
	 * @return addReadingResponse
	 */

	public AddReadingResponse addReading(AddReading addReading) {
		Response response = new Response();
		response.setResponse(false);
		AddReadingResponse addReadingResponse = new AddReadingResponse();
		addReadingResponse.set_return(response);

		// si no habia logeado anteriormente
		if (this.user == null) {
			return addReadingResponse;
		}
		
		// si fue borrado el usuario por otra sesion
		if (users.get(this.user.getName()) == null) {
			return addReadingResponse;
		}

		// comprobar si habia aniadido anteriormente
		for (Book reading : readings.get(this.user.getName())) {
			if (reading.getTitle().equals(addReading.getArgs0().getTitle())) {
				reading.setAuthor(addReading.getArgs0().getAuthor());
				reading.setCalification(addReading.getArgs0().getCalification());
				response.setResponse(true);
				return addReadingResponse;
			}
		}

		readings.get(this.user.getName()).add(addReading.getArgs0());
		response.setResponse(true);
		return addReadingResponse;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param changePassword
	 * @return changePasswordResponse
	 * @throws RemoteException
	 */

	public ChangePasswordResponse changePassword(ChangePassword changePassword)
			throws RemoteException {
		Response response = new Response();
		response.setResponse(false);
		ChangePasswordResponse changePasswordResponse = new ChangePasswordResponse();
		changePasswordResponse.set_return(response);

		// si no se ha logeado anteriormente
		if (this.user == null) {
			return changePasswordResponse;
		}
		
		// si fue borrado el usuario por otra sesion
		if (users.get(this.user.getName()) == null) {
			return changePasswordResponse;
		}

		// si admin se maneja internamente
		if (this.user.getName().equals("admin")) {
			if (this.user.getPwd()
					.equals(changePassword.getArgs0().getOldpwd())) {
				users.put("admin", changePassword.getArgs0().getNewpwd());
				response.setResponse(true);
			}
			return changePasswordResponse;
		}

		// si no coincide el password antiguo que se logeo
		if (!this.user.getPwd().equals(changePassword.getArgs0().getOldpwd())) {
			return changePasswordResponse;
		}

		// llamar al servicio para cambiar el password
		ChangePasswordBackEnd changePasswordBackEnd = new ChangePasswordBackEnd();
		changePasswordBackEnd.setName(this.user.getName());
		changePasswordBackEnd.setOldpwd(users.get(this.user.getName()));
		changePasswordBackEnd.setNewpwd(changePassword.getArgs0().getNewpwd());
		UPMAuthenticationAuthorizationWSSkeletonStub.ChangePassword changePassWord = new UPMAuthenticationAuthorizationWSSkeletonStub.ChangePassword();
		changePassWord.setChangePassword(changePasswordBackEnd);
		ChangePasswordResponseE changePasswordResponseE = stub
				.changePassword(changePassWord);
		if (changePasswordResponseE.get_return().getResult()) {
			users.put(this.user.getName(), changePassword.getArgs0()
					.getNewpwd());
			response.setResponse(true);
		}
		return changePasswordResponse;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param login
	 * @return loginResponse
	 * @throws RemoteException
	 */

	public LoginResponse login(Login login) throws RemoteException {
		LoginResponse loginResponse = new LoginResponse();
		Response response = new Response();
		response.setResponse(false);
		loginResponse.set_return(response);

		// si ya habia logeado anteriormente
		if (user != null) {
			// si fue borrado el usuario por otra sesion
			if (users.get(this.user.getName()) == null) {
				return loginResponse;
			}
			
			if (user.getName().equals(login.getArgs0().getName())) {
				response.setResponse(true);
				this.sessions++;
				return loginResponse;
			} else {
				return loginResponse;
			}
		}

		// si se quiere logear admin
		if (login.getArgs0().getName().equals("admin")) {
			if (login.getArgs0().getPwd().equals(users.get("admin"))) {
				response.setResponse(true);
				this.sessions++;
				this.user = login.getArgs0();
			}
			return loginResponse;
		}

		// no habia logeado y no es admin
		LoginBackEnd loginBackEnd = new LoginBackEnd();
		loginBackEnd.setName(login.getArgs0().getName());
		loginBackEnd.setPassword(login.getArgs0().getPwd());
		UPMAuthenticationAuthorizationWSSkeletonStub.Login loginAuthorization = new UPMAuthenticationAuthorizationWSSkeletonStub.Login();
		loginAuthorization.setLogin(loginBackEnd);
		UPMAuthenticationAuthorizationWSSkeletonStub.LoginResponse loginResponseBackEnd = stub
				.login(loginAuthorization);
		if (loginResponseBackEnd.get_return().getResult()) {
			response.setResponse(true);
			users.put(login.getArgs0().getName(), login.getArgs0().getPwd());
			this.sessions++;
			this.user = login.getArgs0();
			// crear una lista vacio de amigos y de sus lecturas
			if (!friends.containsKey(login.getArgs0().getName())) {
				friends.put(login.getArgs0().getName(), new ArrayList<>());
			}

			if (!readings.containsKey(login.getArgs0().getName())) {
				readings.put(login.getArgs0().getName(), new ArrayList<>());
			}
		}
		return loginResponse;
	}

}
