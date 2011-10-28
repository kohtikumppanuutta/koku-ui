# Copy to server 
mvn -DskipTests clean package
#echo "Copying portlets to GateIn.intra.arcusys.fi"
#scp koku-message-portlet/target/koku-message-new-portlet.war tturunen@jbossportal.intra.arcusys.fi:/home/tturunen
#scp koku-navi-portlet/target/koku-navi-portlet.war tturunen@jbossportal.intra.arcusys.fi:/home/tturunen
#scp koku-taskmanager-portlet/target/koku-taskmanager-portlet.war tturunen@gatein.intra.arcusys.fi:/home/tturunen
#echo "---"
#echo "Copying portlets to jbossportal.intra.arcusys.fi"
#scp koku-message-portlet/target/koku-message-new-portlet.war tturunen@gatein.intra.arcusys.fi:/home/tturunen
#scp koku-navi-portlet/target/koku-navi-portlet.war tturunen@gatein.intra.arcusys.fi:/home/tturunen
#scp koku-taskmanager-portlet/target/koku-taskmanager-portlet.war tturunen@jbossportal.intra.arcusys.fi:/home/tturunen
#echo "---"
echo "Copying portlets to DevIxonos (62.61.65.15)"
scp koku-message-portlet/target/koku-message-new-portlet.war turunto@62.61.65.15:/home/turunto
scp koku-navi-portlet/target/koku-navi-portlet.war turunto@62.61.65.15:/home/turunto
scp koku-taskmanager-portlet/target/koku-taskmanager-portlet.war turunto@62.61.65.15:/home/turunto
echo "---"
echo "Copying portlets to DemoIxonos (62.61.65.16)"
scp koku-message-portlet/target/koku-message-new-portlet.war turunto@62.61.65.16:/home/turunto
scp koku-navi-portlet/target/koku-navi-portlet.war turunto@62.61.65.16:/home/turunto
scp koku-taskmanager-portlet/target/koku-taskmanager-portlet.war turunto@62.61.65.16:/home/turunto
echo "---"


