package pro.documentum.junit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfLoginInfo;

import pro.documentum.junit.auth.IDocumentumCredentials;
import pro.documentum.junit.auth.PropertiesCredentialManager;

/**
 * @author Andrey B. Panfilov <andrey@panfilov.tel>
 */
public abstract class DfcTestSupport extends Assert {

    public static final IDfClient CLIENT;

    static {
        try {
            CLIENT = new DfClientX().getLocalClient();
        } catch (DfException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Rule
    public final TestName _testName = new TestName();

    private IDfSession _session;

    public DfcTestSupport() {
        super();
    }

    public String getTestMethodName() {
        return _testName.getMethodName();
    }

    protected IDfSession getSession() {
        return _session;
    }

    @Before
    public final void setUp() throws Exception {
        doPreSetup();
        doSetup();
        doPostSetup();
    }

    private void doSetup() throws DfException {
        IDocumentumCredentials credentials = new PropertiesCredentialManager(
                null).getCredentials(null, null);
        IDfSessionManager sessionManager = CLIENT.newSessionManager();
        IDfLoginInfo loginInfo = new DfLoginInfo(credentials.getUserName(),
                credentials.getPassword());
        loginInfo.setDomain(credentials.getDomain());
        sessionManager.setIdentity(credentials.getDocbaseName(), loginInfo);
        _session = sessionManager.newSession(credentials.getDocbaseName());
        _session.beginTrans();
    }

    protected void doPreSetup() throws Exception {
        // noop
    }

    protected void doPostSetup() throws Exception {
        // noop
    }

    @After
    public final void tearDown() throws Exception {
        if (_session == null) {
            return;
        }
        try {
            if (_session.isTransactionActive()) {
                _session.abortTrans();
            }
        } finally {
            _session.disconnect();
        }
    }

}
