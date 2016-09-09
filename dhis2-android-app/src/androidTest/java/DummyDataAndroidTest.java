/**
 * Created by george on 9/2/16.
 */
public class DummyDataAndroidTest {

    public static String goodImport = "{\"responseType\":\"ImportSummary\",\"status\":\"SUCCESS\",\"description\":\"Import process completed successfully\",\"importCount\":{\"imported\":0,\"updated\":2,\"ignored\":0,\"deleted\":0},\"dataSetComplete\":\"false\"}";
    public static String badResponse = "Posting data value set:\n" +
            "{ dataSet: 'vjAYdNmic8v',\n" +
            "  orgUnit: 'Foindu-Bo',\n" +
            "  period: '201401',\n" +
            "  completeData: undefined,\n" +
            "  dataValues: \n" +
            "   [ { dataElement: 'FtexyU70UDV', value: '5' },\n" +
            "     { dataElement: 'KPMZxGkiqiQ', value: '5' } ] }\n" +
            "Success: Response {\n" +
            "  domain: null,\n" +
            "  _events: {},\n" +
            "  _eventsCount: 0,\n" +
            "  _maxListeners: undefined,\n" +
            "  res: \n" +
            "   IncomingMessage {\n" +
            "     _readableState: \n" +
            "      ReadableState {\n" +
            "        objectMode: false,\n" +
            "        highWaterMark: 16384,\n" +
            "        buffer: [],\n" +
            "        length: 0,\n" +
            "        pipes: null,\n" +
            "        pipesCount: 0,\n" +
            "        flowing: true,\n" +
            "        ended: true,\n" +
            "        endEmitted: true,\n" +
            "        reading: false,\n" +
            "        sync: true,\n" +
            "        needReadable: false,\n" +
            "        emittedReadable: false,\n" +
            "        readableListening: false,\n" +
            "        resumeScheduled: false,\n" +
            "        defaultEncoding: 'utf8',\n" +
            "        ranOut: false,\n" +
            "        awaitDrain: 0,\n" +
            "        readingMore: false,\n" +
            "        decoder: [Object],\n" +
            "        encoding: 'utf8' },\n" +
            "     readable: false,\n" +
            "     domain: null,\n" +
            "     _events: \n" +
            "      { end: [Object],\n" +
            "        data: [Object],\n" +
            "        error: [Object],\n" +
            "        close: [Function: bound emit] },\n" +
            "     _eventsCount: 4,\n" +
            "     _maxListeners: undefined,\n" +
            "     socket: \n" +
            "      TLSSocket {\n" +
            "        _tlsOptions: [Object],\n" +
            "        _secureEstablished: true,\n" +
            "        _securePending: false,\n" +
            "        _newSessionPending: false,\n" +
            "        _controlReleased: true,\n" +
            "        _SNICallback: null,\n" +
            "        servername: null,\n" +
            "        npnProtocol: undefined,\n" +
            "        alpnProtocol: false,\n" +
            "        authorized: true,\n" +
            "        authorizationError: null,\n" +
            "        encrypted: true,\n" +
            "        _events: [Object],\n" +
            "        _eventsCount: 9,\n" +
            "        connecting: false,\n" +
            "        _hadError: false,\n" +
            "        _handle: null,\n" +
            "        _parent: null,\n" +
            "        _host: 'dev-dhis2-sl.ehealthafrica.org',\n" +
            "        _readableState: [Object],\n" +
            "        readable: false,\n" +
            "        domain: null,\n" +
            "        _maxListeners: undefined,\n" +
            "        _writableState: [Object],\n" +
            "        writable: false,\n" +
            "        allowHalfOpen: false,\n" +
            "        destroyed: true,\n" +
            "        _bytesDispatched: 454,\n" +
            "        _sockname: null,\n" +
            "        _pendingData: null,\n" +
            "        _pendingEncoding: '',\n" +
            "        server: undefined,\n" +
            "        _server: null,\n" +
            "        ssl: null,\n" +
            "        _requestCert: true,\n" +
            "        _rejectUnauthorized: true,\n" +
            "        parser: null,\n" +
            "        _httpMessage: [Object],\n" +
            "        read: [Function],\n" +
            "        _consuming: true,\n" +
            "        _idleNext: null,\n" +
            "        _idlePrev: null,\n" +
            "        _idleTimeout: -1 },\n" +
            "     connection: \n" +
            "      TLSSocket {\n" +
            "        _tlsOptions: [Object],\n" +
            "        _secureEstablished: true,\n" +
            "        _securePending: false,\n" +
            "        _newSessionPending: false,\n" +
            "        _controlReleased: true,\n" +
            "        _SNICallback: null,\n" +
            "        servername: null,\n" +
            "        npnProtocol: undefined,\n" +
            "        alpnProtocol: false,\n" +
            "        authorized: true,\n" +
            "        authorizationError: null,\n" +
            "        encrypted: true,\n" +
            "        _events: [Object],\n" +
            "        _eventsCount: 9,\n" +
            "        connecting: false,\n" +
            "        _hadError: false,\n" +
            "        _handle: null,\n" +
            "        _parent: null,\n" +
            "        _host: 'dev-dhis2-sl.ehealthafrica.org',\n" +
            "        _readableState: [Object],\n" +
            "        readable: false,\n" +
            "        domain: null,\n" +
            "        _maxListeners: undefined,\n" +
            "        _writableState: [Object],\n" +
            "        writable: false,\n" +
            "        allowHalfOpen: false,\n" +
            "        destroyed: true,\n" +
            "        _bytesDispatched: 454,\n" +
            "        _sockname: null,\n" +
            "        _pendingData: null,\n" +
            "        _pendingEncoding: '',\n" +
            "        server: undefined,\n" +
            "        _server: null,\n" +
            "        ssl: null,\n" +
            "        _requestCert: true,\n" +
            "        _rejectUnauthorized: true,\n" +
            "        parser: null,\n" +
            "        _httpMessage: [Object],\n" +
            "        read: [Function],\n" +
            "        _consuming: true,\n" +
            "        _idleNext: null,\n" +
            "        _idlePrev: null,\n" +
            "        _idleTimeout: -1 },\n" +
            "     httpVersionMajor: 1,\n" +
            "     httpVersionMinor: 1,\n" +
            "     httpVersion: '1.1',\n" +
            "     complete: true,\n" +
            "     headers: \n" +
            "      { 'cache-control': 'no-cache=\"set-cookie\"',\n" +
            "        'content-type': 'application/json;charset=UTF-8',\n" +
            "        date: 'Wed, 20 Jul 2016 16:51:52 GMT',\n" +
            "        server: 'Apache-Coyote/1.1',\n" +
            "        'set-cookie': [Object],\n" +
            "        'x-content-type-options': 'nosniff',\n" +
            "        'x-frame-options': 'DENY',\n" +
            "        'x-xss-protection': '1; mode=block',\n" +
            "        'content-length': '243',\n" +
            "        connection: 'Close' },\n" +
            "     rawHeaders: \n" +
            "      [ 'Cache-control',\n" +
            "        'no-cache=\"set-cookie\"',\n" +
            "        'Content-Type',\n" +
            "        'application/json;charset=UTF-8',\n" +
            "        'Date',\n" +
            "        'Wed, 20 Jul 2016 16:51:52 GMT',\n" +
            "        'Server',\n" +
            "        'Apache-Coyote/1.1',\n" +
            "        'Set-Cookie',\n" +
            "        'JSESSIONID=ED3AB5EC92E81B33A29479993111B1CF; Path=/; HttpOnly',\n" +
            "        'Set-Cookie',\n" +
            "        'AWSELB=9D01E541185C97B47F5E1C0164EAE750E32B77DB3C609354050649FA6797B1B9C34D7FB937C6427C1D087C097AF63164A93B3831BE2AE21FAA873EE5E7D108A19407ADC4D2;PATH=/',\n" +
            "        'X-Content-Type-Options',\n" +
            "        'nosniff',\n" +
            "        'X-Frame-Options',\n" +
            "        'DENY',\n" +
            "        'X-XSS-Protection',\n" +
            "        '1; mode=block',\n" +
            "        'Content-Length',\n" +
            "        '243',\n" +
            "        'Connection',\n" +
            "        'Close' ],\n" +
            "     trailers: {},\n" +
            "     rawTrailers: [],\n" +
            "     upgrade: false,\n" +
            "     url: '',\n" +
            "     method: null,\n" +
            "     statusCode: 200,\n" +
            "     statusMessage: 'OK',\n" +
            "     client: \n" +
            "      TLSSocket {\n" +
            "        _tlsOptions: [Object],\n" +
            "        _secureEstablished: true,\n" +
            "        _securePending: false,\n" +
            "        _newSessionPending: false,\n" +
            "        _controlReleased: true,\n" +
            "        _SNICallback: null,\n" +
            "        servername: null,\n" +
            "        npnProtocol: undefined,\n" +
            "        alpnProtocol: false,\n" +
            "        authorized: true,\n" +
            "        authorizationError: null,\n" +
            "        encrypted: true,\n" +
            "        _events: [Object],\n" +
            "        _eventsCount: 9,\n" +
            "        connecting: false,\n" +
            "        _hadError: false,\n" +
            "        _handle: null,\n" +
            "        _parent: null,\n" +
            "        _host: 'dev-dhis2-sl.ehealthafrica.org',\n" +
            "        _readableState: [Object],\n" +
            "        readable: false,\n" +
            "        domain: null,\n" +
            "        _maxListeners: undefined,\n" +
            "        _writableState: [Object],\n" +
            "        writable: false,\n" +
            "        allowHalfOpen: false,\n" +
            "        destroyed: true,\n" +
            "        _bytesDispatched: 454,\n" +
            "        _sockname: null,\n" +
            "        _pendingData: null,\n" +
            "        _pendingEncoding: '',\n" +
            "        server: undefined,\n" +
            "        _server: null,\n" +
            "        ssl: null,\n" +
            "        _requestCert: true,\n" +
            "        _rejectUnauthorized: true,\n" +
            "        parser: null,\n" +
            "        _httpMessage: [Object],\n" +
            "        read: [Function],\n" +
            "        _consuming: true,\n" +
            "        _idleNext: null,\n" +
            "        _idlePrev: null,\n" +
            "        _idleTimeout: -1 },\n" +
            "     _consuming: true,\n" +
            "     _dumped: false,\n" +
            "     req: \n" +
            "      ClientRequest {\n" +
            "        domain: null,\n" +
            "        _events: [Object],\n" +
            "        _eventsCount: 3,\n" +
            "        _maxListeners: undefined,\n" +
            "        output: [],\n" +
            "        outputEncodings: [],\n" +
            "        outputCallbacks: [],\n" +
            "        outputSize: 0,\n" +
            "        writable: true,\n" +
            "        _last: true,\n" +
            "        chunkedEncoding: false,\n" +
            "        shouldKeepAlive: false,\n" +
            "        useChunkedEncodingByDefault: true,\n" +
            "        sendDate: false,\n" +
            "        _removedHeader: [Object],\n" +
            "        _contentLength: 164,\n" +
            "        _hasBody: true,\n" +
            "        _trailer: '',\n" +
            "        finished: true,\n" +
            "        _headerSent: true,\n" +
            "        socket: [Object],\n" +
            "        connection: [Object],\n" +
            "        _header: 'POST /api/dataValueSets HTTP/1.1\\r\\nHost: dev-dhis2-sl.ehealthafrica.org\\r\\nAccept-Encoding: gzip, deflate\\r\\nUser-Agent: node-superagent/1.8.3\\r\\nContent-Type: application/json\\r\\nAccept: application/json\\r\\nAuthorization: Basic R2VvcmdlOkNsb2c5MzR3YWx0NDk3\\r\\nContent-Length: 164\\r\\nConnection: close\\r\\n\\r\\n',\n" +
            "        _headers: [Object],\n" +
            "        _headerNames: [Object],\n" +
            "        _onPendingData: null,\n" +
            "        agent: [Object],\n" +
            "        socketPath: undefined,\n" +
            "        method: 'POST',\n" +
            "        path: '/api/dataValueSets',\n" +
            "        parser: null,\n" +
            "        res: [Circular] },\n" +
            "     text: '{\"responseType\":\"ImportSummary\",\"status\":\"ERROR\",\"description\":\"Import process was aborted\",\"importCount\":{\"imported\":0,\"updated\":0,\"ignored\":0,\"deleted\":0},\"conflicts\":[{\"object\":\"vjAYdNmic8v\",\"value\":\"Org unit not found or not accessible\"}]}',\n" +
            "     read: [Function],\n" +
            "     body: \n" +
            "      { responseType: 'ImportSummary',\n" +
            "        status: 'ERROR',\n" +
            "        description: 'Import process was aborted',\n" +
            "        importCount: [Object],\n" +
            "        conflicts: [Object] } },\n" +
            "  request: \n" +
            "   Request {\n" +
            "     domain: null,\n" +
            "     _events: { end: [Function: bound _clearTimeout] },\n" +
            "     _eventsCount: 1,\n" +
            "     _maxListeners: undefined,\n" +
            "     _agent: false,\n" +
            "     _formData: null,\n" +
            "     method: 'POST',\n" +
            "     url: 'https://dev-dhis2-sl.ehealthafrica.org/api/dataValueSets',\n" +
            "     _header: \n" +
            "      { 'user-agent': 'node-superagent/1.8.3',\n" +
            "        'content-type': 'application/json',\n" +
            "        accept: 'application/json',\n" +
            "        authorization: 'Basic R2VvcmdlOkNsb2c5MzR3YWx0NDk3' },\n" +
            "     header: \n" +
            "      { 'User-Agent': 'node-superagent/1.8.3',\n" +
            "        'Content-Type': 'application/json',\n" +
            "        Accept: 'application/json',\n" +
            "        Authorization: 'Basic R2VvcmdlOkNsb2c5MzR3YWx0NDk3' },\n" +
            "     writable: true,\n" +
            "     _redirects: 0,\n" +
            "     _maxRedirects: 5,\n" +
            "     cookies: '',\n" +
            "     qs: {},\n" +
            "     qsRaw: [],\n" +
            "     _redirectList: [],\n" +
            "     _streamRequest: false,\n" +
            "     _data: '{\"dataSet\":\"vjAYdNmic8v\",\"orgUnit\":\"Foindu-Bo\",\"period\":\"201401\",\"dataValues\":[{\"dataElement\":\"FtexyU70UDV\",\"value\":\"5\"},{\"dataElement\":\"KPMZxGkiqiQ\",\"value\":\"5\"}]}',\n" +
            "     req: \n" +
            "      ClientRequest {\n" +
            "        domain: null,\n" +
            "        _events: [Object],\n" +
            "        _eventsCount: 3,\n" +
            "        _maxListeners: undefined,\n" +
            "        output: [],\n" +
            "        outputEncodings: [],\n" +
            "        outputCallbacks: [],\n" +
            "        outputSize: 0,\n" +
            "        writable: true,\n" +
            "        _last: true,\n" +
            "        chunkedEncoding: false,\n" +
            "        shouldKeepAlive: false,\n" +
            "        useChunkedEncodingByDefault: true,\n" +
            "        sendDate: false,\n" +
            "        _removedHeader: [Object],\n" +
            "        _contentLength: 164,\n" +
            "        _hasBody: true,\n" +
            "        _trailer: '',\n" +
            "        finished: true,\n" +
            "        _headerSent: true,\n" +
            "        socket: [Object],\n" +
            "        connection: [Object],\n" +
            "        _header: 'POST /api/dataValueSets HTTP/1.1\\r\\nHost: dev-dhis2-sl.ehealthafrica.org\\r\\nAccept-Encoding: gzip, deflate\\r\\nUser-Agent: node-superagent/1.8.3\\r\\nContent-Type: application/json\\r\\nAccept: application/json\\r\\nAuthorization: Basic R2VvcmdlOkNsb2c5MzR3YWx0NDk3\\r\\nContent-Length: 164\\r\\nConnection: close\\r\\n\\r\\n',\n" +
            "        _headers: [Object],\n" +
            "        _headerNames: [Object],\n" +
            "        _onPendingData: null,\n" +
            "        agent: [Object],\n" +
            "        socketPath: undefined,\n" +
            "        method: 'POST',\n" +
            "        path: '/api/dataValueSets',\n" +
            "        parser: null,\n" +
            "        res: [Object] },\n" +
            "     protocol: 'https:',\n" +
            "     host: 'dev-dhis2-sl.ehealthafrica.org',\n" +
            "     _callback: [Function],\n" +
            "     res: \n" +
            "      IncomingMessage {\n" +
            "        _readableState: [Object],\n" +
            "        readable: false,\n" +
            "        domain: null,\n" +
            "        _events: [Object],\n" +
            "        _eventsCount: 4,\n" +
            "        _maxListeners: undefined,\n" +
            "        socket: [Object],\n" +
            "        connection: [Object],\n" +
            "        httpVersionMajor: 1,\n" +
            "        httpVersionMinor: 1,\n" +
            "        httpVersion: '1.1',\n" +
            "        complete: true,\n" +
            "        headers: [Object],\n" +
            "        rawHeaders: [Object],\n" +
            "        trailers: {},\n" +
            "        rawTrailers: [],\n" +
            "        upgrade: false,\n" +
            "        url: '',\n" +
            "        method: null,\n" +
            "        statusCode: 200,\n" +
            "        statusMessage: 'OK',\n" +
            "        client: [Object],\n" +
            "        _consuming: true,\n" +
            "        _dumped: false,\n" +
            "        req: [Object],\n" +
            "        text: '{\"responseType\":\"ImportSummary\",\"status\":\"ERROR\",\"description\":\"Import process was aborted\",\"importCount\":{\"imported\":0,\"updated\":0,\"ignored\":0,\"deleted\":0},\"conflicts\":[{\"object\":\"vjAYdNmic8v\",\"value\":\"Org unit not found or not accessible\"}]}',\n" +
            "        read: [Function],\n" +
            "        body: [Object] },\n" +
            "     response: [Circular],\n" +
            "     _timeout: 0,\n" +
            "     called: true },\n" +
            "  req: \n" +
            "   ClientRequest {\n" +
            "     domain: null,\n" +
            "     _events: { drain: [Function], error: [Function], response: [Function] },\n" +
            "     _eventsCount: 3,\n" +
            "     _maxListeners: undefined,\n" +
            "     output: [],\n" +
            "     outputEncodings: [],\n" +
            "     outputCallbacks: [],\n" +
            "     outputSize: 0,\n" +
            "     writable: true,\n" +
            "     _last: true,\n" +
            "     chunkedEncoding: false,\n" +
            "     shouldKeepAlive: false,\n" +
            "     useChunkedEncodingByDefault: true,\n" +
            "     sendDate: false,\n" +
            "     _removedHeader: { 'content-length': false },\n" +
            "     _contentLength: 164,\n" +
            "     _hasBody: true,\n" +
            "     _trailer: '',\n" +
            "     finished: true,\n" +
            "     _headerSent: true,\n" +
            "     socket: \n" +
            "      TLSSocket {\n" +
            "        _tlsOptions: [Object],\n" +
            "        _secureEstablished: true,\n" +
            "        _securePending: false,\n" +
            "        _newSessionPending: false,\n" +
            "        _controlReleased: true,\n" +
            "        _SNICallback: null,\n" +
            "        servername: null,\n" +
            "        npnProtocol: undefined,\n" +
            "        alpnProtocol: false,\n" +
            "        authorized: true,\n" +
            "        authorizationError: null,\n" +
            "        encrypted: true,\n" +
            "        _events: [Object],\n" +
            "        _eventsCount: 9,\n" +
            "        connecting: false,\n" +
            "        _hadError: false,\n" +
            "        _handle: null,\n" +
            "        _parent: null,\n" +
            "        _host: 'dev-dhis2-sl.ehealthafrica.org',\n" +
            "        _readableState: [Object],\n" +
            "        readable: false,\n" +
            "        domain: null,\n" +
            "        _maxListeners: undefined,\n" +
            "        _writableState: [Object],\n" +
            "        writable: false,\n" +
            "        allowHalfOpen: false,\n" +
            "        destroyed: true,\n" +
            "        _bytesDispatched: 454,\n" +
            "        _sockname: null,\n" +
            "        _pendingData: null,\n" +
            "        _pendingEncoding: '',\n" +
            "        server: undefined,\n" +
            "        _server: null,\n" +
            "        ssl: null,\n" +
            "        _requestCert: true,\n" +
            "        _rejectUnauthorized: true,\n" +
            "        parser: null,\n" +
            "        _httpMessage: [Circular],\n" +
            "        read: [Function],\n" +
            "        _consuming: true,\n" +
            "        _idleNext: null,\n" +
            "        _idlePrev: null,\n" +
            "        _idleTimeout: -1 },\n" +
            "     connection: \n" +
            "      TLSSocket {\n" +
            "        _tlsOptions: [Object],\n" +
            "        _secureEstablished: true,\n" +
            "        _securePending: false,\n" +
            "        _newSessionPending: false,\n" +
            "        _controlReleased: true,\n" +
            "        _SNICallback: null,\n" +
            "        servername: null,\n" +
            "        npnProtocol: undefined,\n" +
            "        alpnProtocol: false,\n" +
            "        authorized: true,\n" +
            "        authorizationError: null,\n" +
            "        encrypted: true,\n" +
            "        _events: [Object],\n" +
            "        _eventsCount: 9,\n" +
            "        connecting: false,\n" +
            "        _hadError: false,\n" +
            "        _handle: null,\n" +
            "        _parent: null,\n" +
            "        _host: 'dev-dhis2-sl.ehealthafrica.org',\n" +
            "        _readableState: [Object],\n" +
            "        readable: false,\n" +
            "        domain: null,\n" +
            "        _maxListeners: undefined,\n" +
            "        _writableState: [Object],\n" +
            "        writable: false,\n" +
            "        allowHalfOpen: false,\n" +
            "        destroyed: true,\n" +
            "        _bytesDispatched: 454,\n" +
            "        _sockname: null,\n" +
            "        _pendingData: null,\n" +
            "        _pendingEncoding: '',\n" +
            "        server: undefined,\n" +
            "        _server: null,\n" +
            "        ssl: null,\n" +
            "        _requestCert: true,\n" +
            "        _rejectUnauthorized: true,\n" +
            "        parser: null,\n" +
            "        _httpMessage: [Circular],\n" +
            "        read: [Function],\n" +
            "        _consuming: true,\n" +
            "        _idleNext: null,\n" +
            "        _idlePrev: null,\n" +
            "        _idleTimeout: -1 },\n" +
            "     _header: 'POST /api/dataValueSets HTTP/1.1\\r\\nHost: dev-dhis2-sl.ehealthafrica.org\\r\\nAccept-Encoding: gzip, deflate\\r\\nUser-Agent: node-superagent/1.8.3\\r\\nContent-Type: application/json\\r\\nAccept: application/json\\r\\nAuthorization: Basic R2VvcmdlOkNsb2c5MzR3YWx0NDk3\\r\\nContent-Length: 164\\r\\nConnection: close\\r\\n\\r\\n',\n" +
            "     _headers: \n" +
            "      { host: 'dev-dhis2-sl.ehealthafrica.org',\n" +
            "        'accept-encoding': 'gzip, deflate',\n" +
            "        'user-agent': 'node-superagent/1.8.3',\n" +
            "        'content-type': 'application/json',\n" +
            "        accept: 'application/json',\n" +
            "        authorization: 'Basic R2VvcmdlOkNsb2c5MzR3YWx0NDk3',\n" +
            "        'content-length': 164 },\n" +
            "     _headerNames: \n" +
            "      { host: 'Host',\n" +
            "        'accept-encoding': 'Accept-Encoding',\n" +
            "        'user-agent': 'User-Agent',\n" +
            "        'content-type': 'Content-Type',\n" +
            "        accept: 'Accept',\n" +
            "        authorization: 'Authorization',\n" +
            "        'content-length': 'Content-Length' },\n" +
            "     _onPendingData: null,\n" +
            "     agent: \n" +
            "      Agent {\n" +
            "        domain: null,\n" +
            "        _events: [Object],\n" +
            "        _eventsCount: 1,\n" +
            "        _maxListeners: undefined,\n" +
            "        defaultPort: 443,\n" +
            "        protocol: 'https:',\n" +
            "        options: [Object],\n" +
            "        requests: {},\n" +
            "        sockets: [Object],\n" +
            "        freeSockets: {},\n" +
            "        keepAliveMsecs: 1000,\n" +
            "        keepAlive: false,\n" +
            "        maxSockets: Infinity,\n" +
            "        maxFreeSockets: 256,\n" +
            "        maxCachedSessions: 100,\n" +
            "        _sessionCache: [Object] },\n" +
            "     socketPath: undefined,\n" +
            "     method: 'POST',\n" +
            "     path: '/api/dataValueSets',\n" +
            "     parser: null,\n" +
            "     res: \n" +
            "      IncomingMessage {\n" +
            "        _readableState: [Object],\n" +
            "        readable: false,\n" +
            "        domain: null,\n" +
            "        _events: [Object],\n" +
            "        _eventsCount: 4,\n" +
            "        _maxListeners: undefined,\n" +
            "        socket: [Object],\n" +
            "        connection: [Object],\n" +
            "        httpVersionMajor: 1,\n" +
            "        httpVersionMinor: 1,\n" +
            "        httpVersion: '1.1',\n" +
            "        complete: true,\n" +
            "        headers: [Object],\n" +
            "        rawHeaders: [Object],\n" +
            "        trailers: {},\n" +
            "        rawTrailers: [],\n" +
            "        upgrade: false,\n" +
            "        url: '',\n" +
            "        method: null,\n" +
            "        statusCode: 200,\n" +
            "        statusMessage: 'OK',\n" +
            "        client: [Object],\n" +
            "        _consuming: true,\n" +
            "        _dumped: false,\n" +
            "        req: [Circular],\n" +
            "        text: '{\"responseType\":\"ImportSummary\",\"status\":\"ERROR\",\"description\":\"Import process was aborted\",\"importCount\":{\"imported\":0,\"updated\":0,\"ignored\":0,\"deleted\":0},\"conflicts\":[{\"object\":\"vjAYdNmic8v\",\"value\":\"Org unit not found or not accessible\"}]}',\n" +
            "        read: [Function],\n" +
            "        body: [Object] } },\n" +
            "  links: {},\n" +
            "  text: '{\"responseType\":\"ImportSummary\",\"status\":\"ERROR\",\"description\":\"Import process was aborted\",\"importCount\":{\"imported\":0,\"updated\":0,\"ignored\":0,\"deleted\":0},\"conflicts\":[{\"object\":\"vjAYdNmic8v\",\"value\":\"Org unit not found or not accessible\"}]}',\n" +
            "  body: \n" +
            "   { responseType: 'ImportSummary',\n" +
            "     status: 'ERROR',\n" +
            "     description: 'Import process was aborted',\n" +
            "     importCount: { imported: 0, updated: 0, ignored: 0, deleted: 0 },\n" +
            "     conflicts: [ [Object] ] },\n" +
            "  files: {},\n" +
            "  buffered: true,\n" +
            "  headers: \n" +
            "   { 'cache-control': 'no-cache=\"set-cookie\"',\n" +
            "     'content-type': 'application/json;charset=UTF-8',\n" +
            "     date: 'Wed, 20 Jul 2016 16:51:52 GMT',\n" +
            "     server: 'Apache-Coyote/1.1',\n" +
            "     'set-cookie': \n" +
            "      [ 'JSESSIONID=ED3AB5EC92E81B33A29479993111B1CF; Path=/; HttpOnly',\n" +
            "        'AWSELB=9D01E541185C97B47F5E1C0164EAE750E32B77DB3C609354050649FA6797B1B9C34D7FB937C6427C1D087C097AF63164A93B3831BE2AE21FAA873EE5E7D108A19407ADC4D2;PATH=/' ],\n" +
            "     'x-content-type-options': 'nosniff',\n" +
            "     'x-frame-options': 'DENY',\n" +
            "     'x-xss-protection': '1; mode=block',\n" +
            "     'content-length': '243',\n" +
            "     connection: 'Close' },\n" +
            "  header: \n" +
            "   { 'cache-control': 'no-cache=\"set-cookie\"',\n" +
            "     'content-type': 'application/json;charset=UTF-8',\n" +
            "     date: 'Wed, 20 Jul 2016 16:51:52 GMT',\n" +
            "     server: 'Apache-Coyote/1.1',\n" +
            "     'set-cookie': \n" +
            "      [ 'JSESSIONID=ED3AB5EC92E81B33A29479993111B1CF; Path=/; HttpOnly',\n" +
            "        'AWSELB=9D01E541185C97B47F5E1C0164EAE750E32B77DB3C609354050649FA6797B1B9C34D7FB937C6427C1D087C097AF63164A93B3831BE2AE21FAA873EE5E7D108A19407ADC4D2;PATH=/' ],\n" +
            "     'x-content-type-options': 'nosniff',\n" +
            "     'x-frame-options': 'DENY',\n" +
            "     'x-xss-protection': '1; mode=block',\n" +
            "     'content-length': '243',\n" +
            "     connection: 'Close' },\n" +
            "  statusCode: 200,\n" +
            "  status: 200,\n" +
            "  statusType: 2,\n" +
            "  info: false,\n" +
            "  ok: true,\n" +
            "  redirect: false,\n" +
            "  clientError: false,\n" +
            "  serverError: false,\n" +
            "  error: false,\n" +
            "  accepted: false,\n" +
            "  noContent: false,\n" +
            "  badRequest: false,\n" +
            "  unauthorized: false,\n" +
            "  notAcceptable: false,\n" +
            "  forbidden: false,\n" +
            "  notFound: false,\n" +
            "  charset: 'UTF-8',\n" +
            "  type: 'application/json',\n" +
            "  setEncoding: [Function: bound ],\n" +
            "  redirects: [] }\n";

    public static String profileData ="{\"id\":\"mQCdPdUipPj\",\"username\":\"admin\",\"firstName\":\"admin\",\"surname\":\"admin\",\"email\":\"admin@sl.ehealthafrica.org\",\"phoneNumber\":\"+2327600000\",\"gender\":\"gender_male\",\"settings\":{\"keyDbLocale\":null,\"keyMessageSmsNotification\":\"true\",\"keyUiLocale\":\"en\",\"keyAnalysisDisplayProperty\":\"name\",\"keyMessageEmailNotification\":\"true\"}}";

    public static String proocessedProfileData = "{\"firstName\":\"admin\",\"surname\":\"admin\",\"email\":\"admin@sl.ehealthafrica.org\",\"phoneNumber\":\"+2327600000\",\"introduction\":\"\",\"jobTitle\":\"\",\"gender\":\"gender_male\",\"birthday\":\"\",\"nationality\":\"\",\"employer\":\"\",\"education\":\"\",\"interests\":\"\",\"languages\":\"\"}";

    public static final String PERIOD = "2016W33";
    public static final String PERIOD_LABEL = "Week 33";
    public static final String ORG_UNIT_ID = "tWi9XVPNNue";
    public static final String ORG_UNIT_LABEL = "Benkeh MCHP";
    public static final String FORM_ID = "w9c9UJ3Prus";
    public static final String FORM_LABEL = "IDSR Weekly Disease Report(WDR)_old";
    public static final String DATASETINFO_KEY = "tWi9XVPNNuew9c9UJ3Prus2016W33";

    public static final String GOOD_GET_FORM_RESPONSE = "{\"iQgaTATK59f\":{\"id\":\"iQgaTATK59f\",\"label\":\"Bonthe\",\"level\":2,\"parent\":\"Plmg8ikyfrK\",\"dataSets\":[{\"id\":\"C9bv64TkafG\",\"label\":\"Sabeen Test Form\"},{\"id\":\"PKV9PQ1YXc0\",\"label\":\"Jasper Test Form\"},{\"id\":\"UvsIrD50wUy\",\"label\":\"weekly disease reporting form\"},{\"id\":\"w9c9UJ3Prus\",\"label\":\"IDSR Weekly Disease Report(WDR)_old\"},{\"id\":\"PILVKnAwy4Q\",\"label\":\"eIDSR Report\"},{\"id\":\"hONZE29XBxq\",\"label\":\"Lutz Test Form\"},{\"id\":\"EB5hz0gpUnf\",\"label\":\"Paris Test Form\"},{\"id\":\"h6PK4knx7uW\",\"label\":\"Week report form Test\"},{\"id\":\"rq0LNr72Ndo\",\"label\":\"IDSR Weekly Disease Report(WDR)\"}]}}";


    public static final String GOOD_GET_FORM_RESPONSE_ARRAY = "[{\"iQgaTATK59f\":{\"id\":\"iQgaTATK59f\",\"label\":\"Bonthe\",\"level\":2,\"parent\":\"Plmg8ikyfrK\",\"dataSets\":[{\"id\":\"C9bv64TkafG\",\"label\":\"Sabeen Test Form\"},{\"id\":\"PKV9PQ1YXc0\",\"label\":\"Jasper Test Form\"},{\"id\":\"UvsIrD50wUy\",\"label\":\"weekly disease reporting form\"},{\"id\":\"w9c9UJ3Prus\",\"label\":\"IDSR Weekly Disease Report(WDR)_old\"},{\"id\":\"PILVKnAwy4Q\",\"label\":\"eIDSR Report\"},{\"id\":\"hONZE29XBxq\",\"label\":\"Lutz Test Form\"},{\"id\":\"EB5hz0gpUnf\",\"label\":\"Paris Test Form\"},{\"id\":\"h6PK4knx7uW\",\"label\":\"Week report form Test\"},{\"id\":\"rq0LNr72Ndo\",\"label\":\"IDSR Weekly Disease Report(WDR)\"}]}}]";

}
