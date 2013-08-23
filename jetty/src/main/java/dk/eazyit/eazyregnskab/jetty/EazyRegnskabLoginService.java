package dk.eazyit.eazyregnskab.jetty;

import dk.eazyit.eazyregnskab.security.PasswordEncoder;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.security.MappedLoginService;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.Loader;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.security.Credential;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author
 */
public class EazyRegnskabLoginService extends MappedLoginService {

    private static final Logger LOG = Log.getLogger(JDBCLoginService.class);

    private String _config;
    private String _jdbcDriver;
    private String _url;
    private String _userName;
    private String _password;
    private String _userTableKey;
    private String _userTablePasswordField;
    String _userRoleTableRoleKey;
    private int _cacheTime;
    private long _lastHashPurge;
    private Connection _con;
    private String _userSql;
    private String _roleSql;

    public EazyRegnskabLoginService() throws IOException {
    }

    @Override
    public UserIdentity login(String username, Object credentials) {

        PasswordEncoder passwordEncoder = PasswordEncoder.getInstance();
        String unDigestedPassword = (String) credentials;
        String digestedPassword = passwordEncoder.encode(unDigestedPassword, username);

        return super.login(username, digestedPassword);
    }

    @Override
    protected void doStart() throws Exception {
        Properties properties = new Properties();
        Resource resource = Resource.newResource(_config);
        properties.load(resource.getInputStream());

        _jdbcDriver = properties.getProperty("jdbcdriver");
        _url = properties.getProperty("url");
        _userName = properties.getProperty("username");
        _password = properties.getProperty("password");
        String _userTable = properties.getProperty("usertable");
        _userTableKey = properties.getProperty("usertablekey");
        String _userTableUserField = properties.getProperty("usertableuserfield");
        _userTablePasswordField = properties.getProperty("usertablepasswordfield");
        String _userRoleTable = properties.getProperty("userroletable");
        String _userRoleTableUserKey = properties.getProperty("userroletableuserkey");
        _userRoleTableRoleKey = properties.getProperty("userroletablerolekey");
        _cacheTime = new Integer(properties.getProperty("cachetime"));

        if (_jdbcDriver == null || _jdbcDriver.equals("")
                || _url == null
                || _url.equals("")
                || _userName == null
                || _userName.equals("")
                || _password == null
                || _cacheTime < 0) {
            LOG.warn("UserRealm " + getName() + " has not been properly configured");
        }
        _cacheTime *= 1000;
        _lastHashPurge = 0;
        _userSql = "select " + _userTableKey + "," + _userTablePasswordField + " from " + _userTable + " where " + _userTableUserField + " = ?";
        _roleSql = "select r." + _userRoleTableRoleKey
                + " from "
                + _userRoleTable
                + " r where r."
                + _userRoleTableUserKey
                + " = ?";

        Loader.loadClass(this.getClass(), _jdbcDriver).newInstance();
        super.doStart();
    }


    /* ------------------------------------------------------------ */
    public String getConfig() {
        return _config;
    }

        /* ------------------------------------------------------------ */

    /**
     * Load JDBC connection configuration from properties file.
     *
     * @param config Filename or url of user properties file.
     */
    public void setConfig(String config) {
        if (isRunning())
            throw new IllegalStateException("Running");
        _config = config;
    }

        /* ------------------------------------------------------------ */

    /**
     * (re)Connect to database with parameters setup by loadConfig()
     */
    public void connectDatabase() {
        try {
            Class.forName(_jdbcDriver);
            _con = DriverManager.getConnection(_url, _userName, _password);
        } catch (SQLException e) {
            LOG.warn("UserRealm " + getName() + " could not connect to database; will try later", e);
        } catch (ClassNotFoundException e) {
            LOG.warn("UserRealm " + getName() + " could not connect to database; will try later", e);
        }
    }

    /* ------------------------------------------------------------ */
    @Override
    protected void loadUsers() {
    }

    /* ------------------------------------------------------------ */
    @Override
    protected UserIdentity loadUser(String username) {
        try {
            if (null == _con)
                connectDatabase();

            if (null == _con)
                throw new SQLException("Can't connect to database");

            PreparedStatement stat = _con.prepareStatement(_userSql);
            stat.setObject(1, username);
            ResultSet rs = stat.executeQuery();

            if (rs.next()) {
                int key = rs.getInt(_userTableKey);
                String credentials = rs.getString(_userTablePasswordField);
                stat.close();

                stat = _con.prepareStatement(_roleSql);
                stat.setInt(1, key);
                rs = stat.executeQuery();
                List<String> roles = new ArrayList<String>();
                while (rs.next())
                    roles.add(rs.getString(_userRoleTableRoleKey));

                stat.close();
                return putUser(username, Credential.getCredential(credentials), roles.toArray(new String[roles.size()]));
            }
        } catch (SQLException e) {
            LOG.warn("UserRealm " + getName() + " could not load user information from database", e);
            closeConnection();
        }
        return null;
    }

    /**
     * Close an existing connection
     */
    private void closeConnection() {
        if (_con != null) {
            if (LOG.isDebugEnabled()) LOG.debug("Closing db connection for JDBCUserRealm");
            try {
                _con.close();
            } catch (Exception e) {
                LOG.ignore(e);
            }
        }
        _con = null;
    }

}