# openid-showcase
Demonstrate shows the flow of open id by listing app steps.

Framework used <br>
<ul> <li>Spring boot</li>
<li>scribejava</li>
<li>lombook</li>
<li>keycloak</li>
</ul>

## Prerequisite 
-  Set up keycloak IDP  ( https://www.keycloak.org/docs/latest/server_admin/#configuring-realms).
- Grab client id , cleint secret and realm name and add it in application.yml (src/main/resources ) file .
- DO not forgot to set correct redirectURI in keycloak otherwise you will get BAD Request from keycloak on redirection.
    - Redirect URI is * http://localhost:8090/openid-showcase *
    
## Running the application
  - Run `sh run.sh` 
    - This will start spring boot application and will give you the link to obtain the authorization code
 - Copy the URL and paste in browser.
 - You will be redirected to keycloak login page.
 - Login using the user and credentials you created in Prerequisite ( 1 ).
 - Post sucessfull login , you will again be redirected , 
    - just grab the code request parameter value from browser address bar ( &code=XXXXX).
 - Paste on console 
 - You will see the access token on the console.
 - The application will then try to use this access token to access user profile endpoint.
 - Prints response from server.
