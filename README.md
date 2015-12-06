# Beschreibung
ant-joomla ist eine Ant-Erweiterung, mit der man Joomla-Erweiterungen per Kommandozeile installieren (und auch deinstallieren) kann. Insbesondere 
während der Entwicklung eine Joomla-Erweiterung kann dies hilfreich sein. 

Für einen produktiven Einsatz ist diese Ant-Erweiterung NICHT geeignet. Im produktiven Umfeld gibt es geeigneter Wege, um Joomla-Erweiterungen 
komfortabel zu installieren.  

Das Ganze nutzt das üblich HTML-Admin-Interfac von Joomla, da keine andere Schnittstelle verfügbar ist.

Die Idee stammt von
	http://joomboss.com/products/ant-joomla/documentation
	
Der Original-Code unterstützt Joomla 3.x nicht. Daher habe ich das ganze entsprechend angepasst. 

# Voraussetzungen

* JDK 7 oder besser
* Ant, getestet und benutzt unter 1.9.4
Alle benötigen Abhängigkeiten sind im Repository enthalten und befinden sich im Ordner lib/ 

# Bauen

Mit

    ant 
wird die Ant-Erweiterung gebaut.
    
# Installation

Mit

    ant install
wird die Erweiterung nach $HOME/joomla-ant installiert.

# Nutzung

## Einbinden in eine Ant-Build-Datei

    <path id="ant-joomla.lib.path">
      <fileset dir="${user.home}/ant-joomla/lib" includes="*.jar"/>
    </path>

    <taskdef name="joomla-install-extension" classname="com.joomboss.InstallExtensionTask" classpathref="ant-joomla.lib.path"/>
    <taskdef name="joomla-remove-extension"  classname="com.joomboss.RemoveExtensionTask"  classpathref="ant-joomla.lib.path"/>
    
## Einrichten eines Users

Der User, der für die Installation von Joomla-Erweiterungen werden soll, muss 
* Admin-Rechte besitzen und
* deren Defaultsprache muss Englisch sein.
