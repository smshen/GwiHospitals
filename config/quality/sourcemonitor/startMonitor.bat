echo off
echo Source Monitor Start...
echo  ===Command Path:%1
echo  ===Build Path:%2
sourceMonitor /C %1\sourceMonitorCommand.xml
msxsl %2\reports\SourceMonitorReport.xml "%SOURCE_MONITOR_HOME%\SourceMonitorSummaryGeneration.xsl" -o %2\reports\SourceMonitorSummaryGeneration.xml
msxsl %2\reports\SourceMonitorSummaryGeneration.xml "%SOURCE_MONITOR_HOME%\SourceMonitor.xsl" -o %2\reports\SourceMonitorResult.html
echo Source Monitor Finished.