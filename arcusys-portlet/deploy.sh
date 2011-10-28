mvn clean package &&
# Copy to Jboss
cp koku-message-portlet/target/koku-message-portlet.war /home/tturunen/Programs/koku/jboss/jboss-portal-2.7.2/server/default/deploy
cp koku-navi-portlet/target/koku-navi-portlet.war /home/tturunen/Programs/koku/jboss/jboss-portal-2.7.2/server/default/deploy
cp koku-layout-portlet/target/koku-layout-portlet.war /home/tturunen/Programs/koku/jboss/jboss-portal-2.7.2/server/default/deploy
cp koku-taskmanager-portlet/target/koku-taskmanager-portlet.war /home/tturunen/Programs/koku/jboss/jboss-portal-2.7.2/server/default/deploy &&

# Copy to JBoss EPP
cp koku-message-portlet/target/koku-message-portlet.war /home/tturunen/Programs/koku/gatein.ixonos/jboss-as/server/default/deploy
cp koku-navi-portlet/target/koku-navi-portlet.war /home/tturunen/Programs/koku/gatein.ixonos/jboss-as/server/default/deploy
cp koku-layout-portlet/target/koku-layout-portlet.war /home/tturunen/Programs/koku/gatein.ixonos/jboss-as/server/default/deploy
cp koku-taskmanager-portlet/target/koku-taskmanager-portlet.war /home/tturunen/Programs/koku/gatein.ixonos/jboss-as/server/default/deploy 
