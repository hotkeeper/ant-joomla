ant-joomla ist eine ANT-Erweiterung, mit der man Joomla-Erweiterungen per Komandozeile installieren 
(und auch deinstallieren) kann.

Das Ganze nutzt das üblich HTML-Admin-Interfac von Joomla. 

Die Idee stammt von
	http://joomboss.com/products/ant-joomla/documentation
	
Leider tut dies nicht mit Joomla 1.7/2.5. So habe ich das ganze entsprechend angepasst.

Alles weite findet man unter
	http://www.joomla-hklein.de/?p=1018

Einbinden in eine Ant-Build-Datei

<path id="ant-joomla.lib.path">
		<fileset dir="${user.home}/ant-joomla/lib" includes="*.jar"/>
</path>

<taskdef name="joomla-install-extension" classname="com.joomboss.InstallExtensionTask" classpathref="ant-joomla.lib.path"/>
<taskdef name="joomla-remove-extension"  classname="com.joomboss.RemoveExtensionTask"  classpathref="ant-joomla.lib.path"/>