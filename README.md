# Beschreibung
ant-joomla ist eine ANT-Erweiterung, mit der man Joomla-Erweiterungen per Komandozeile installieren (und auch deinstallieren) kann.

Das Ganze nutzt das üblich HTML-Admin-Interfac von Joomla. 

Die Idee stammt von
	http://joomboss.com/products/ant-joomla/documentation
	
Der Original-Code unterstützt Joomla 3.x nicht. Daher habe ich das ganze entsprechend angepasst.

# Bauen


# Nutzung

Einbinden in eine Ant-Build-Datei

<path id="ant-joomla.lib.path">
		<fileset dir="${user.home}/ant-joomla/lib" includes="*.jar"/>
</path>

<taskdef name="joomla-install-extension" classname="com.joomboss.InstallExtensionTask" classpathref="ant-joomla.lib.path"/>
<taskdef name="joomla-remove-extension"  classname="com.joomboss.RemoveExtensionTask"  classpathref="ant-joomla.lib.path"/>