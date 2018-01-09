package com.adjust.gps
{
	import flash.external.ExtensionContext;
	import flash.system.Capabilities;

	public class AdjustGPS
	{
		/* Public */
		
		public static function get isSupported():Boolean
		{
			return !!context;
		}
		
		/* Private */
		
		// Static initializer
		{
			init();	
		}
		
		private static var context:ExtensionContext;
		
		private static function init():void
		{
			var version:String = Capabilities.version.substr(0,3);
			
			switch (version)
			{
				case 'AND':
				{
					context = ExtensionContext.createExtensionContext('com.adjust.gps', '');
					break;
				}
			}
		}
	}
}
