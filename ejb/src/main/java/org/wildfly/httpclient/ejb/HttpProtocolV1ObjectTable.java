/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2017 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wildfly.httpclient.ejb;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.rmi.RemoteException;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.CreateException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.EJBMetaData;
import javax.ejb.EJBObject;
import javax.ejb.EJBTransactionRequiredException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.FinderException;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.ejb.NoSuchEJBException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionRequiredException;
import javax.transaction.TransactionRolledbackException;

import org.jboss.ejb.client.AbstractEJBMetaData;
import org.jboss.ejb.client.Affinity;
import org.jboss.ejb.client.AttachmentKey;
import org.jboss.ejb.client.AttachmentKeys;
import org.jboss.ejb.client.BasicSessionID;
import org.jboss.ejb.client.ClusterAffinity;
import org.jboss.ejb.client.EJBClientInvocationContext;
import org.jboss.ejb.client.EJBClientPermission;
import org.jboss.ejb.client.EJBHandle;
import org.jboss.ejb.client.EJBHomeHandle;
import org.jboss.ejb.client.EJBHomeLocator;
import org.jboss.ejb.client.EJBLocator;
import org.jboss.ejb.client.EJBMethodLocator;
import org.jboss.ejb.client.EntityEJBLocator;
import org.jboss.ejb.client.EntityEJBMetaData;
import org.jboss.ejb.client.NodeAffinity;
import org.jboss.ejb.client.SerializedEJBInvocationHandler;
import org.jboss.ejb.client.SessionID;
import org.jboss.ejb.client.StatefulEJBLocator;
import org.jboss.ejb.client.StatefulEJBMetaData;
import org.jboss.ejb.client.StatelessEJBLocator;
import org.jboss.ejb.client.StatelessEJBMetaData;
import org.jboss.ejb.client.TransactionID;
import org.jboss.ejb.client.URIAffinity;
import org.jboss.ejb.client.UnknownSessionID;
import org.jboss.ejb.client.UserTransactionID;
import org.jboss.ejb.client.XidTransactionID;
import org.jboss.marshalling.ByteWriter;
import org.jboss.marshalling.ObjectTable;
import org.jboss.marshalling.Unmarshaller;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
final class HttpProtocolV1ObjectTable implements ObjectTable {
    static final HttpProtocolV1ObjectTable INSTANCE = new HttpProtocolV1ObjectTable();

    private static final Map<Object, ByteWriter> writers;
    /**
     * Do NOT change the order of this list.
     */
    private static final Object[] objects = {
            TransactionID.PRIVATE_DATA_KEY,
            Affinity.NONE,
            Affinity.WEAK_AFFINITY_CONTEXT_KEY,
            EJBClientInvocationContext.PRIVATE_ATTACHMENTS_KEY,
            AttachmentKeys.TRANSACTION_ID_KEY,
            AttachmentKeys.WEAK_AFFINITY,
            AttachmentKeys.COMPRESS_RESPONSE,
            AttachmentKeys.RESPONSE_COMPRESSION_LEVEL,
            AttachmentKeys.TRANSACTION_KEY,
            AttachmentKeys.HINTS_DISABLED,
            AttachmentKeys.VIEW_CLASS_DATA_COMPRESSION_HINT_ATTACHMENT_KEY,
            AttachmentKeys.VIEW_METHOD_DATA_COMPRESSION_HINT_ATTACHMENT_KEY,
            Throwable.class.getName(),
            Exception.class.getName(),
            RuntimeException.class.getName(),
            EJBLocator.class.getName(),
            EJBHomeLocator.class.getName(),
            StatelessEJBLocator.class.getName(),
            StatefulEJBLocator.class.getName(),
            EntityEJBLocator.class.getName(),
            EJBHandle.class.getName(),
            EJBHomeHandle.class.getName(),
            SerializedEJBInvocationHandler.class.getName(),
            SessionID.class.getName(),
            UnknownSessionID.class.getName(),
            BasicSessionID.class.getName(),
            UserTransactionID.class.getName(),
            XidTransactionID.class.getName(),
            EJBHome.class.getName(),
            EJBObject.class.getName(),
            Handle.class.getName(),
            HomeHandle.class.getName(),
            EJBMetaData.class.getName(),
            RemoteException.class.getName(),
            NoSuchEJBException.class.getName(),
            NoSuchEntityException.class.getName(),
            CreateException.class.getName(),
            DuplicateKeyException.class.getName(),
            EJBAccessException.class.getName(),
            EJBException.class.getName(),
            EJBTransactionRequiredException.class.getName(),
            EJBTransactionRolledbackException.class.getName(),
            FinderException.class.getName(),
            RemoveException.class.getName(),
            ObjectNotFoundException.class.getName(),
            Future.class.getName(),
            SystemException.class.getName(),
            RollbackException.class.getName(),
            TransactionRequiredException.class.getName(),
            TransactionRolledbackException.class.getName(),
            NotSupportedException.class.getName(),
            InvalidTransactionException.class.getName(),
            StackTraceElement.class.getName(),
            SessionID.Serialized.class.getName(),
            TransactionID.class.getName(),
            TransactionID.Serialized.class.getName(),
            Affinity.class.getName(),
            NodeAffinity.class.getName(),
            ClusterAffinity.class.getName(),
            URIAffinity.class.getName(),
            EJBMethodLocator.class.getName(),
            AbstractEJBMetaData.class.getName(),
            StatelessEJBMetaData.class.getName(),
            StatefulEJBMetaData.class.getName(),
            EntityEJBMetaData.class.getName(),
            AttachmentKey.class.getName(),
            EJBClientPermission.class.getName(),
            AsyncResult.class.getName(),
            "detailMessage",
            "cause",
            "stackTrace",
            "value",
            "suppressedExceptions",
            "ejbCreate",
            "ejbRemove",
            "ejbHome",
            "remove",
            "ejbActivate",
            "ejbPassivate",
            "ejbLoad",
            "ejbStore",
    };

    static {
        final Map<Object, ByteWriter> map = new IdentityHashMap<Object, ByteWriter>();
        for (int i = 0, length = objects.length; i < length; i++) {
            map.put(objects[i], new ByteWriter((byte) i));
        }
        writers = map;
    }

    public Writer getObjectWriter(final Object object) throws IOException {
        return writers.get(object);
    }

    public Object readObject(final Unmarshaller unmarshaller) throws IOException, ClassNotFoundException {
        int idx = unmarshaller.readUnsignedByte();
        if (idx >= objects.length) {
            throw new InvalidObjectException("ObjectTable " + this.getClass().getName() + " cannot find an object for object index " + idx);
        }
        return objects[idx];
    }
}
