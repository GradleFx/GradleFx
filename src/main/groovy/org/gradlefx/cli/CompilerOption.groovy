/*
* Copyright (c) 2011 the original author or authors
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.gradlefx.cli

public enum CompilerOption {
    
    //---------------//
    //---- mxmlc ----//
    //---------------//
    
    /**
     * Enables accessibility features when compiling the application or SWC file.
     * 
     * type: boolean
     * default: false
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7f60.html">Accessible applications</a>
     */
    ACCESSIBLE("-accesible"), 
    
    /**
     * Sets the file encoding for ActionScript files.
     * 
     * type: string
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ac4.html">Setting the file encoding</a>
     */
    ACTIONSCRIPT_FILE_ENCODING("-actionscript-file-encoding"), 
    
    /**
     * Checks if a source-path entry is a subdirectory of another source-path entry. 
     * It helps make the package names of MXML components unambiguous.
     * 
     * type: boolean
     */
    ALLOW_SOURCE_PATH_OVERLAP("-allow-source-path-overlap"),
    
    /**
     * Use the ActionScript 3.0 class-based object model for greater performance and better error reporting. 
     * In the class-based object model, most built-in functions are implemented as fixed methods of classes. 
     * If you set this value to false, you must set the <code>es</code> option to true.
     * 
     * type: boolean
     * default: true
     */
    AS3("-as3"),
    
    /**
     * Prints detailed compile times to the standard output.
     * 
     * type: boolean
     * default: true
     */
    BENCHMARK("-benchmark"),
    
    /**
     * Enables or disables SWF file compression.
     * 
     * type: boolean
     * default: false when <code>debug</code> true and vice versa
     */
    COMPRESS("-compress"),
    
    /**
     * The name of the config file being used. Possible values are: flex, air and airmobile.
     * 
     * type: string
     * default: flex
     */
    CONFIGNAME("+configname"),
    
    /**
     * Sets the value of the {context.root} token, which is often used in channel definitions in the 
     * flex-services.xml file and other settings in the flex-config.xml file.
     * 
     * type: context-path
     * default: null
     */
    CONTEXT_ROOT("-context-root"),
    
    /**
     * Sets metadata in the resulting SWF file.
     * 
     * type: name
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ac3.html">Adding metadata to SWF files</a>
     */
    CONTRIBUTOR("-contributor"),
    
    /**
     * Sets metadata in the resulting SWF file.
     *
     * type: name
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ac3.html">Adding metadata to SWF files</a>
     */
    CREATOR("-creator"),
   
    /**
     * Sets metadata in the resulting SWF file.
     *
     * type: string
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ac3.html">Adding metadata to SWF files</a>
     */
    DATE("-date"),
    
    /**
     * Generates a debug SWF file. This file includes line numbers and filenames of all the source files. 
     * When a run-time error occurs, the stacktrace shows these line numbers and filenames. 
     * This information is used by the command-line debugger and the Flex Builder debugger. 
     * Enabling the debug option generates larger SWF files and also disables compilation optimization.
     * If you set this option to true, Flex also sets the verbose-stacktraces option to true.
     * 
     * type: boolean
     * default: false for mxmlc; true for compc
     */
    DEBUG("-debug"),
    
    /**
     * Lets you engage in remote debugging sessions with the Flash IDE. 
     * 
     * type: string
     */
    DEBUG_PASSWORD("-debug-password"),
    
    /**
     * Sets the application�s frame rate.
     * 
     * type: int
     * default: 24
     */
    DEFAULT_FRAME_RATE("-default-frame-rate"),
    
    /**
     * Defines the application�s script execution limits.
     * The max-recursion-depth value specifies the maximum depth of Adobe Flash Player call stack before Flash Player stops. 
     * This is essentially the stack overflow limit. 
     * The max-execution-time value specifies the maximum duration, in seconds, that an ActionScript event handler can execute 
     * before Flash Player assumes that it is hung, and aborts it. You cannot set this value above 60 seconds.
     * You can override these settings in the application.
     * 
     * type: max-recursion-depth(int) max-execution-time(int)
     * default: 1000 60
     */
    DEFAULT_SCRIPT_LIMITS("-default-script-limits"),
    
    /**
     * Defines the default application size, in pixels.
     * 
     * type: width(int) height(int)
     */
    DEFAULT_SIZE("-default-size"),
    
    /**
     * Inserts CSS files into the output the same way that a per-SWC defaults.css file works, 
     * but without having to re-archive the SWC file to test each change.
     * CSS files included in the output with this option have a higher precedence than default CSS files in existing SWCs. 
     * For example, a CSS file included with this option overrides definitions in framework.swc�s defaults.css file, 
     * but it has the same overall precedence as other included CSS files inside the SWC file.
     * This option does not actually insert the CSS file into the SWC file; it simulates it. 
     * When you finish developing the CSS file, you should rebuild the SWC file with the new integrated CSS file.
     * This option takes one or more files. The precedence for multiple CSS files included with this option is from first to last.
     * 
     * type: filename [, ...]
     */
    DEFAULT_CSS_FILES("-defaults-css-files"),
    
    /**
     * Defines the location of the default style sheet. 
     * Setting this option overrides the implicit use of the defaults.css style sheet in the framework.swc file.
     * 
     * type: string
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7fee.html">Styles and themes</a>
     */
    DEFAULT_CSS_URL("-defaults-css-url"),
    
    /**
     * Defines a global constant. The value is evaluated at compile time and exists as a constant within the application. 
     * A common use of inline constants is to set values that are used to include or exclude blocks of code, 
     * such as debugging or instrumentation code. This is known as conditional compilation.
     * To set multiple conditionals on the command-line, use the define option more than once.
     * 
     * type: NAMESPACE::variable,value (STRING::string,boolean)
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7abd.html">Using conditional compilation</a>
     */
    DEFINE("-define"),
    
    /**
     * Sets metadata in the resulting SWF file.
     *
     * type: string
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ac3.html">Adding metadata to SWF files</a>
     */
    DESCRIPTION("-description"),
    
    /**
     * Outputs the compiler options in the flex-config.xml file to the target path.
     * 
     * type: filename
     */
    DUMP_CONFIG("-dump-config"),
    
    /**
     * Instructs the compiler to use the ECMAScript edition 3 prototype-based object model to allow dynamic overriding of prototype properties. 
     * In the prototype-based object model, built-in functions are implemented as dynamic properties of prototype objects.
     * Using the ECMAScript edition 3 prototype-based object model lets you use untyped properties and functions in your application code. 
     * As a result, if you set the value of the <code>es</code> compiler option to true, you must set the <code>strict</code> compiler option to false. 
     * Otherwise, the compiler will throw errors. 
     * If you set this option to true, you must also set the value of the <code>as3</code> compiler option to false.
     * 
     * type: boolean
     * default: false
     */
    ES("-es"),
    
    /**
     * Sets a list of classes to exclude from linking when compiling a SWF file. 
     * This option provides compile-time link checking for external references that are dynamically linked.
     * 
     * type: classname [...]
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7f1e.html">About linking</a>
     */
    EXTERNS("-externs"),
    
    /**
     * Specifies a list of SWC files or directories to exclude from linking when compiling a SWF file. 
     * This option provides compile-time link checking for external components that are dynamically linked. 
     * By default, the libs/player/playerglobal.swc file is linked as an external library. 
     * This library is built into Flash Player. 
     * You can use the += operator to append the new SWC file to the list of external libraries.
     * 
     * type: path-element [...]
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7f1e.html">About linking</a>
     */
    EXTERNAL_LIBRARY_PATH("-external-library-path"),
    
    /**
     * Sets the default value that determines whether embedded fonts use advanced anti-aliasing information when rendering the font. 
     * Setting the value of the advanced-anti-aliasing property in a style sheet overrides this value.
     * 
     * type: boolean
     * default: false
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7f9e.html">Fonts</a>
     */
    FONTS_ADVANCED_ANTIALIASING("-fonts.advanced-anti-aliasing"),
    
    /**
     * Specifies the range of Unicode settings for that language.
     *
     * type: language(string) range(unicode)
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7f9e.html">Fonts</a>
     */
    FONTS_LANGUAGE_RANGE("-fonts.languages.language-range"),
   
    /**
     * Sets the location of the local font snapshot file. The file contains system font data.
     *
     * type: filepath
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7f9e.html">Fonts</a>
     */
    FONTS_LOCAL_SNAPSHOT("-fonts.local-fonts-snapshot"),
    
    /**
     * Defines the font manager. The default is flash.fonts.JREFontManager. You can also use the flash.fonts.BatikFontManager.
     *
     * type: manager-class
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7f9e.html">Fonts</a>
     */
    FONTS_MANAGERS("-fonts.managers"),
    
    /**
     * Sets the maximum number of fonts to keep in the server cache.
     *
     * type: int
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7f9e.html">Fonts</a>
     */
    FONTS_MAX_CACHED("-fonts.max-cached-fonts"),
    
    /**
     * Sets the maximum number of character glyph-outlines to keep in the server cache for each font face.
     *
     * type: int
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7f9e.html">Fonts</a>
     */
    FONTS_MAX_GLYPHS_PER_FACE("-fonts.max-glyphs-per-face"),
    
    /**
     * Specifies a SWF file frame label with a sequence of class names that are linked onto the frame; 
     * for example: <code>-frame=MyLabel,Class1,Class2</code> 
     * This option lets you add asset factories that stream in after the application that then publish their interfaces with the ModuleManager class. 
     * The advantage to doing this is that the application starts faster than it would have if the assets had been included in the code, 
     * but does not require moving the assets to an external SWF file.
     *
     * type: label,class_name[,...]
     */
    FRAME("-frames.frame"),
    
    /**
     * Toggles the generation of an IFlexBootstrap-derived loader class. 
     * 
     * type: boolean
     */
    GENERATE_FRAME_LOADER("-generate-frame-loader"),
    
    /**
     * Enables the headless implementation of the Flex compiler. 
     * This sets the following: System.setProperty("java.awt.headless", "true")
     * The headless setting (java.awt.headless=true) is required to use fonts and SVG on UNIX systems without X Windows.
     * 
     * type: boolean
     */
    HEADLESS_SERVER("-headless-server"),
    
    /**
     * Prints usage information to the standard output.
     */
    HELP("-help"),
    
    /**
     * Include only classes that are inheritance dependencies of classes that are included with the include-classes compiler option. 
     * You might use this compiler option if you are creating a custom RSL and want to externalize as many classes as possible. 
     * 
     * type: boolean
     */
    INCLUDE_INHERITANCE_DEPENDENCIES_ONLY("-include-inheritance-dependencies-only"),
    
    /**
     * Links all classes inside a SWC file to the resulting application SWF file, regardless of whether or not they are used. 
     * Contrast this option with the library-path option that includes only those classes that are referenced at compile time. 
     * To link one or more classes whether or not they are used and not an entire SWC file, use the includes option. 
     * This option is commonly used to specify resource bundles.
     * 
     * type: library [...]
     */
    INCLUDE_LIBRARIES("-include-libraries"),
    
    /**
     * Specifies the resource bundles to link into a resource module. 
     * All resource bundles specified with this option must be in the compiler�s source path. 
     * You specify this using the <code>source-path</code> compiler option. 
     * 
     * type: bundle [...]
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7fcf.html">Resource Bundles</a>
     */
    INCLUDE_RESOURCE_BUNDLES("-include-resource-bundles"),
    
    /**
     * Links one or more classes to the resulting application SWF file, whether or not those classes are required at compile time. 
     * To link an entire SWC file rather than individual classes, use the <code>include-libraries</code> option.
     * 
     * type: class [...]
     */
    INCLUDES("-includes"),
    
    /**
     * Enables incremental compilation.
     * 
     * type: boolean
     * default: false
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ed3.html">About incremental compilation</a>
     */
    INCREMENTAL("-incremental"),
    
    /**
     * Enables per-module styling. 
     * You typically only use this option if you want to set styles in a module that is loaded into your main application.
     * 
     * type: boolean
     * default: true
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7f22.html">Modular applications</a>
     */
    ISOLATE_STYLES("-isolate-styles"),
    
    /**
     * Specifies custom metadata that you want to keep. 
     * If you want to preserve the default metadata, you should use the += operator to append your custom metadata, 
     * rather than the = operator which replaces the default metadata.
     * 
     * type: classname [...]
     * default:
     * <ul>
     *  <li>Bindable</li>
     *  <li>Managed</li>
     *  <li>ChangeEvent</li>
     *  <li>NonCommittingChangeEvent</li>
     *  <li>Transient</li>
     * </ul>
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ab2.html">About metadata tags</a>
     */
    KEEP_AS3_METADATA("-keep-as3-metadata"),
    
    /**
     * Instructs the compiler to keep a style sheet�s type selector in a SWF file, even if that type (the class) is not used in the application. 
     * This is useful when you have a modular application that loads other applications. 
     * For example, the loading SWF file might define a type selector for a type used in the loaded (or, target) SWF file. 
     * If you set this option to true when compiling the loading SWF file, then the target SWF file will have access to that type selector when it is loaded. 
     * If you set this option to false, the compiler will not include that type selector in the loading SWF file at compile time. 
     * As a result, the styles will not be available to the target SWF file.
     * 
     * type: boolean
     */
    KEEP_ALL_TYPE_SELECTORS("-keep-all-type-selectors"),
    
    /**
     * Determines whether to keep the generated ActionScript class files. 
     * The generated class files include stubs and classes that are generated by the compiler and used to build the SWF file. 
     * When using the application compiler, the default location of the files is the /generated subdirectory, which is directly below the target MXML file. 
     * If the /generated directory does not exist, the compiler creates one. When using the compc component compiler, 
     * the default location of the /generated directory is relative to the output of the SWC file. 
     * The default names of the primary generated class files are <code>filename-generated.as</code> and <code>filename-interface.as</code>.
     * 
     * type: boolean
     * default: false
     */
    KEEP_GENERATED_ACTIONSCRIPT("-keep-generated-actionscript"),
    
    /**
     * Sets metadata in the resulting SWF file.
     * 
     * type: code
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ac3.html">Adding metadata to SWF files</a>
     */
    LANGUAGE("-language"),
    
    /**
     * Links SWC files to the resulting application SWF file. 
     * The compiler only links in those classes for the SWC file that are required. 
     * You can specify a directory or individual SWC files. 
     * The default value of the library-path option includes all SWC files in the libs and libs/player directories, 
     * plus the current locale directory. These are required. 
     * To point to individual classes or packages rather than entire SWC files, use the source-path option. 
     * If you set the value of the library-path as an option of the command-line compiler, 
     * you must also explicitly add the framework.swc and locale SWC files. 
     * Your new entry is not appended to the library-path but replaces it, unless you use the += operator.
     * On the command line, you use the += operator to append the new argument to the list of existing SWC files. 
     * In a configuration file, you can set the append attribute of the library-path tag to true to indicate that 
     * the values should be appended to the library path rather than replace existing default entries.
     * 
     * type: path-element [...]
     */
    LIBRARY_PATH("-library-path"),
    
    /**
     * Defines the license key to use when compiling.
     * 
     * type: product_name,license_key
     */
    LICENSE("-license"),
    
    /**
     * Prints linking information to the specified output file. 
     * This file is an XML file that contains <def>, <pre>, and <ext> symbols showing linker dependencies in the final SWF file. 
     * The output of this command can be used as input to the <code>load-externs</code> option.
     * 
     * type: filename
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7adc.html">Examining linker dependencies</a>
     */
    LINK_REPORT("-link-report"),
    
    /**
     * Specifies the location of the configuration file that defines compiler options. 
     * If you specify a configuration file, you can override individual options by setting them on the command line. 
     * All relative paths in the configuration file are relative to the location of the configuration file itself. 
     * Use the += operator to chain this configuration file to other configuration files. 
     * 
     * type: filename
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7fca.html">About configuration files</a>
     */
    LOAD_CONFIG("-load-config"),
    
    /**
     * Specifies the location of an XML file that contains <def>, <pre>, and <ext> symbols to omit from linking when compiling a SWF file. 
     * The XML file uses the same syntax as the one produced by the <code>link-report</code> option.
     * This option provides compile-time link checking for external components that are dynamically linked.
     * 
     * type: filename [...]
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7adc.html">Examining linker dependencies</a>
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7f1e.html">About linking</a>
     */
    LOAD_EXTERNS("-load-externs"),
    
    /**
     * Specifies one or more locales to be compiled into the SWF file. 
     * If you do not specify a locale, then the compiler uses the default locale from the flex-config.xml file. 
     * You can append additional locales to the default locale by using the += operator. 
     * If you remove the default locale from the flex-config.xml file, and do not specify one on the command line, 
     * then the compiler will use the machine�s locale.
     * 
     * type: locale[,...]
     * default: en_US, or null without flex-config.xml
     */
    LOCALE("-locale"),
    
    /**
     * Sets metadata in the resulting SWF file.
     * 
     * type: text language
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ac3.html">Adding metadata to SWF files</a>
     */
    LOCALIZED_DESCRIPTION("-localized-description"),
    
    /**
     * Sets metadata in the resulting SWF file.
     * 
     * type: text language
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ac3.html">Adding metadata to SWF files</a>
     */
    LOCALIZED_TITLE("-localized-title"),
    
    /**
     * Specifies the version of the Flex compiler that the output should be compatible with. 
     * This option affects some behavior such as the layout rules, padding and gaps, skins, and other style settings. 
     * In addition, it affects the rules for parsing properties files. 
     * The following example instructs the application to compile with the Flex 3 rules for these behaviors:
     * <code>-compatibility-version=3.0.0</code> 
     * Possible values for this compiler option are defined as constants in the FlexVersion class. 
     * If you set this value to 3.0.0, you must also use a theme in your application that is compatible with version 3 of the SDK.
     * 
     * type: version
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ede.html">Backward compatibility</a>
     */
    MXML_COMPATIBILITY_VERSION("-mxml.compatibility-version"),
    
    /**
     * Specifies the minimum version of the SDK that the application uses. This is typically used when generating SWC files. 
     * 
     * type: version
     */
    MXML_MINIMUM_SUPPORTED_VERSION("-mxml.minimum-supported-version"),
    
    /**
     * Determines whether you want the compiler to ensure that type selectors have a qualified namespace in the CSS files.
     * 
     * type: boolean
     * default: true
     */
    MXML_QUALIFIED_TYPE_SELECTORS("-mxml.qualified-type-selectors"),
    
    /**
     * Specifies a namespace for the MXML file. 
     * You must include a URI and the location of the manifest file that defines the contents of this namespace. 
     * This path is relative to the MXML file.
     * 
     * type: uri manifest
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7aa8.html">About manifest files</a>
     */
    NAMESPACE("-namespaces.namespace"),
    
    /**
     * Enables the ActionScript optimizer. 
     * This optimizer reduces file size and increases performance by optimizing the SWF file�s bytecode.
     * 
     * type: boolean
     * default: true
     */
    OPTIMIZE("-optimize"),
    
    /**
     * Enables trace() statements from being written to the flashlog.txt file.
     * 
     * type: boolean
     * default: false
     */
    OMIT_TRACE_STATEMENTS("-omit-trace-statements"),
    
    /**
     * Specifies the output path and filename for the resulting file. 
     * If you omit this option, the compiler saves the SWF file to the directory where the target file is located. 
     * The default SWF filename matches the target filename, but with a SWF file extension. 
     * If you use a relative path to define the filename, it is always relative to the current working directory, not the target MXML application root. 
     * The compiler creates extra directories based on the specified filename if those directories are not present.
     * When using this option with the component compiler, the output is a SWC file rather than a SWF file, 
     * unless you set the directory option to true. In that case, the output is a directory with the contents of the SWC file. 
     * The name of the directory is that value of the ouput option.
     * 
     * type: filename
     * default: [target filename].swf for mxmlc; [target filename].swc for compc; asdoc-output for asdoc
     */
    OUTPUT("-output"),
    
    /**
     * Specify a download progress bar for your application. 
     * The value must be the name of a class that implements the IPreloaderDisplay interface.
     * 
     * type: classname
     * default:
     * <ul>
     *  <li>�mx.preloaders.SparkDownloadProgressBar� when compatibility-version is 4.0.0 or greater</li>
     *  <li>�mx.preloaders.DownloadProgressBar� when compatibility-version is less than 4.0.0</li>
     * </ul>
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7e3c.html">Showing the download progress of an application</a>
     */
    PRELOADER("-preloader"),
    
    /**
     * Sets metadata in the resulting SWF file.
     *
     * type: name
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ac3.html">Adding metadata to SWF files</a>
     */
    PUBLISHER("-publisher"),
    
    /**
     * Defines the metadata for the resulting SWF file. 
     * The value of this option overrides any metadata-related compiler options such as contributor, creator, date, and description.
     * 
     * type: XML
     */
    RAW_METADATA("-raw-metadata"),
    
    /**
     * Instructs the compiler to only include RSLs that are used by the application.
     * 
     * type: boolean
     * default: true
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7fd1.html">Runtime Shared Libraries</a>
     */
    REMOVE_UNUSED_RSLS("-remove-unused-rsls"),
    
    /**
     * Prints a list of resource bundles that are used by the current application to a file named with the filename argument. 
     * You then use this list as input that you specify with the <code>include-resource-bundles</code> option to create a resource module.
     * 
     * type: filename
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7fcf.html">Resource Bundles</a>
     */
    RESOURCE_BUNDLE_LIST("-resource-bundle-list"),
    
    /**
     * Specifies a list of runtime shared libraries (RSLs) to use for this application. RSLs are dynamically-linked at run time. 
     * The compiler externalizes the contents of the application that you are compiling that overlap with the RSL. 
     * You specify the location of the SWF file relative to the deployment location of the application. 
     * For example, if you store a file named library.swf file in the web_root/libraries directory on the web server, 
     * and the application in the web root, you specify libraries/library.swf. 
     * This compiler argument is included for backwards compatibility with Flex 3 applications. 
     * For Flex 4 applications, use the <code>runtime-shared-library-path</code> option.
     * 
     * type: rsl-url [...]
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7fd1.html">Runtime Shared Libraries</a>
     */
    @Deprecated
    RUNTIME_SHARED_LIBRARIES("-runtime-shared-libraries"),
    
    /**
     * Specifies the location of a runtime shared library (RSL). 
     * The compiler externalizes the contents of the application that you are compiling that overlap with the RSL.
     * 
     * type: path-element,rsl-url[,policy-file-url,failover-url,...]
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7fd1.html">Runtime Shared Libraries</a>
     */
    RUNTIME_SHARED_LIBRARY_PATH("-runtime-shared-library-path"),
    
    /**
     * Controls which domain an RSL is loaded into at runtime. 
     * The path-element is the path of the SWC library. 
     * To specify an RSL with this option, you must also define it in the runtime-shared-library-path option. 
     * The application-domain-target is the domain that the RSL should be loaded into. 
     * Valid values for application-domain-target are default, current, parent, and top-level.
     * 
     * type: path-element,application-domain-target
     * default: null,default
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7f22.html">Modular applications</a>
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7d14.html">Developing and loading sub-applications</a>
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7fd1.html">Runtime Shared Libraries</a>
     */
    RUNTIME_SHARED_LIBRARY_SETTINGS_APPLICATION_DOMAIN("-runtime-shared-library-settings.application-domain"),
    
    /**
     * Forces an RSL to be included, regardless of whether it is used by the application. 
     * This is useful if all the links to a particular class in an RSL are soft references, 
     * or if you anticipate a module or sub-application to need a class that is not used by the main application. 
     * The path-element is the path of the SWC library. You can specify more than one path-element by using a comma-delimited list of RSLs. 
     * To specify an RSL with this option, you must also define it in the runtime-shared-library-path option.
     * 
     * type: path-element
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7fd1.html">Runtime Shared Libraries</a>
     */
    RUNTIME_SHARED_LIBRARY_SETTINGS_FORCE_RSLS("-runtime-shared-library-settings.force-rsls"),
    
    /**
     * Specifies the location of the services-config.xml file. This file is used by LiveCycle Data Services ES.
     * 
     * type: filename
     */
    SERVICES("-services"),
    
    /**
     * Shows warnings for ActionScript classes. 
     * 
     * type: boolean
     * default: true
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7eaf.html">Viewing warnings and errors</a>
     */
    SHOW_ACTIONSCRIPT_WARNINGS("-show-actionscript-warnings"),
    
    /**
     * Shows a warning when Flash Player cannot detect changes to a bound property.
     * 
     * type: boolean
     * default: true
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7eaf.html">Viewing warnings and errors</a>
     */
    SHOW_BINDING_WARNINGS("-show-binding-warnings"),
    
    /**
     * Shows a warning when a style property is set in CSS on a component that does not support that property. 
     * The warning can be a result of the theme not supporting the style property, the component not declaring it, or the component excluding it.
     * 
     * type: boolean
     * default: true
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7eaf.html">Viewing warnings and errors</a>
     */
    SHOW_INVALID_CSS_PROPERTY_WARNINGS("-show-invalid-css-property-warnings"),
    
    /**
     * Shows warnings when you try to embed a font with a family name that is the same as the operating system font name. 
     * The compiler normally warns you that you are shadowing a system font. Set this option to false to disable the warnings. 
     * 
     * type: boolean
     * default: true
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7eaf.html">Viewing warnings and errors</a>
     */
    SHOW_SHADOWED_DEVICE_FONT_WARNINGS("-show-shadowed-device-font-warnings"),
    
    /**
     * Shows warnings when a type selector in a style sheet or <fx:Style> block is not used by any components in the application. 
     * This warning does not detect whether a condition is met for descendant selectors.
     * 
     * type: boolean
     * default: true
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7eaf.html">Viewing warnings and errors</a>
     */
    SHOW_UNUSED_TYPE_SELECTOR_WARNINGS("-show-unused-type-selector-warnings"),
    
    /**
     * Creates a report that summarizes the size of each type of data within an application SWF file. 
     * Types of data include media files, fonts, shapes, and ActionScript. 
     * For example: <code>mxmlc -size-report=myreport.xml MyApp.mxml</code> 
     * The file format of the output is XML. 
     * While the format has some similarities to the output of the link-report option, 
     * the size report cannot be used as input to the load-externs option. 
     * This option is useful if you are optimizing your application and want to see how big areas of your application are.
     * 
     * type: filename
     */
    SIZE_REPORT("-size-report"),
    
    /**
     * Adds directories or files to the source path. 
     * The Flex compiler searches directories in the source path for MXML, AS, or CSS source files 
     * that are used in your applications and includes those that are required at compile time. 
     * You can use wildcards to include all files and subdirectories of a directory. 
     * To link an entire library SWC file and not individual classes or directories, use the library-path option. 
     * The source path is also used as the search path for the component compiler�s include-classes and include-resource-bundles options. 
     * You can also use the += operator to append the new argument to the list of existing source path entries.
     * 
     * type: path-element [...]
     * default:
     * <ul>
     *  <li>If source-path is empty, the target file�s directory will be added to source-path.</li>
     *  <li>If source-path is not empty and if the target file�s directory is a subdirectory of 
     *  one of the directories in source-path, source-path remains unchanged.</li>
     *  <li>If source-path is not empty and if the target file�s directory is not a subdirectory of 
     *  any one of the directories in source-path, the target file�s directory is prepended to source-path.</li>
     * </ul>
     */
    SOURCE_PATH("-source-path"),
    
    /**
     * Determines whether to compile against libraries statically or use RSLs. 
     * Set this option to true to ignore the RSLs specified by the runtime-shared-library-path option. 
     * Set this option to false to use the RSLs. 
     * This option is useful so that you can quickly switch between a statically and dynamically linked application 
     * without having to change the runtime-shared-library-path option, which can be verbose, or edit the configuration files. 
     * 
     * type: boolean
     * default: true
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7fd1.html">Runtime Shared Libraries</a>
     */
    STATIC_LINK_RUNTIME_SHARED_LIBRARIES("-static-link-runtime-shared-libraries"),
    
    /**
     * Prints undefined property and function calls; 
     * also performs compile-time type checking on assignments and options supplied to method calls.
     * 
     * type: boolean
     * default: true
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7eaf.html">Viewing warnings and errors</a>
     */
    STRICT("-strict"),
    
    /**
     * Specifies the SWF file format version of the output SWF file. 
     * Features requiring a later version of the SWF file format are not compiled into the application. 
     * This is different from the Player version in that it refers to the SWF specification versioning scheme. 
     * 
     * type: int
     * default: 14 for Flex 4.6 (and the default Player version is 11.1)
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ee0.html">Targeting Flash Player versions</a>
     */
    SWF_VERSION("-swf-version"),
    
    /**
     * Specifies the version of Flash Player that you want to target with the application. 
     * Features requiring a later version of Flash Player are not compiled into the application.
     * The player_version parameter has the following format: major_version.minor_version.revision 
     * The major_version is required while minor_version and revision are optional. 
     * For Flex 4.0 and 4.1, the minimum value is 10.0.0. 
     * If you do not specify the minor_version or revision, then the compiler uses zeros. 
     * For Flex 4.5, the default value is 10.2.0. For Flex 4.6, the default value is 11.1.
     * If you do not explicitly set the value of this option, the compiler uses the default from the flex-config.xml file. 
     * The value in flex-config.xml is the version of Flash Player that shipped with the SDK. 
     * This option is useful if your application�s audience has a specific Player and cannot upgrade. 
     * You can use this option to �downgrade� your application for that audience. 
     * 
     * type: major_version[.minor_version.revision]
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ee0.html">Targeting Flash Player versions</a>
     */
    TARGET_PLAYER("-target-player"),
    
    /**
     * Specifies a list of theme files to use with this application. 
     * Theme files can be SWC files with CSS files inside them or CSS files. 
     * 
     * type: filename [...]
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7fee.html">Styles and themes</a>
     */
    THEME("-theme"),
    
    /**
     * Sets metadata in the resulting SWF file.
     *
     * type: name
     * <a href="http://help.adobe.com/en_US/flex/using/WS2db454920e96a9e51e63e3d11c0bf69084-7ac3.html">Adding metadata to SWF files</a>
     */
    TITLE("-title"),
    
    /**
     * Specifies the locale to use when reporting compiler errors and warnings. 
     * Valid values are the language code (such as "ja" or "en") 
     * or the language code plus country code (such as "ja_JP" or "en_US"), depending on your system's configuration.
     * 
     * type: locale
     */
    TOOLS_LOCALE("-tools-locale"),
    
    /**
     * Specifies whether hardware acceleration is used to copy graphics to the screen (if such acceleration is available). 
     * This option only applies to applications running in the standalone Flash Player. 
     * For applications running in a browser, setting use-direct-blit to true is equivalent to setting wmode to "direct" in the HTML wrapper. 
     * For AIR applications, use the renderMode application descriptor tag.
     * 
     * type: boolean
     * default: false
     */
    USE_DIRECT_BLIT("-use-direct-blit"),
    
    /**
     * Specifies whether GPU (Graphics Processing Unit) acceleration is used when drawing graphics (if such acceleration is available). 
     * This option only applies to applications running in the standalone Flash Player. 
     * For applications running in a browser, setting use-gpu to true is equivalent to setting wmode to "gpu" in the HTML wrapper.
     * For AIR applications, use the renderMode application descriptor tag.
     *  
     * type: boolean
     * default: false
     */
    USE_GPU("-use-gpu"),
    
    /**
     * Specifies that the current application uses network services. 
     * When the use-network property is set to false, the application can access the local filesystem 
     * (for example, use the XML.load() method with file: URLs) but not network services. 
     * In most circumstances, the value of this property should be true.
     * 
     * type: boolean
     * default: true
     */
    USE_NETWORK("-use-network"),
    
    /**
     * Enables resource bundles. Set to true to instruct the compiler to process the contents of the [ResourceBundle] metadata tag.
     * 
     * type: boolean
     * default: true
     */
    USE_RESOURCE_BUNDLE_METADATA("-use-resource-bundle-metadata"),
    
    /**
     * Generates source code that includes line numbers. When a run-time error occurs, the stacktrace shows these line numbers. 
     * Enabling this option generates larger SWF files. 
     * Enabling this option does not generate a debug SWF file. To do that, you must set the debug option to true.
     * 
     * type: boolean
     * default: false
     */
    VERBOSE_STACKTRACES("-verbose-stacktraces"),
    
    /**
     * Instructs the application to check the digest of the RSL SWF file against the digest that was compiled into the application at compile time. 
     * This is a security measure that lets you load RSLs from remote domains or different sub-domains. 
     * It also lets you enforce versioning of your RSLs by forcing an application�s digest to match the RSL�s digest. 
     * If the digests are out of sync, you must recompile your application or load a different RSL SWF file.
     * 
     * type: boolean
     * default: false
     */
    VERIFY_DIGESTS("-verify-digests"),
    
    /**
     * Returns the version of the compiler
     * 
     * type: none
     */
    VERSION("-version"),
    
    /**
     * Enables specified warnings.
     * 
     * type: boolean
     */
    WARNING_TYPE("-warn-warning_type"),
    
    /**
     * Enables all warnings.
     * 
     * type: boolean
     * default: true
     */
    WARNINGS("-warnings"),
    
    //---------------//
    //---- compc ----//
    //---------------//
    
    /**
     * Writes a digest to the catalog.xml of a library. 
     * Use this when the library will be used as a cross-domain RSL or when you want to enforce the versioning of RSLs. 
     * 
     * type: boolean
     * default: true
     */
    COMPUTE_DIGEST("-compute-digest"),
    
    /**
     * Outputs the SWC file in an open directory format rather than a SWC file. 
     * You use this option with the output option to specify a destination directory, as the following example shows:
     * <code>compc -directory=true -output=destination_directory</code> 
     * You typically use this option when you create RSLs because you must extract the library.swf file from the SWC file before deployment.
     * 
     * type: boolean
     */
    DIRECTORY("-directory"),
    
    /**
     * Specifies classes to include in the SWC file. You provide the class name (for example, MyClass) rather than 
     * the file name (for example, MyClass.as) to the file for this option. As a result, all classes specified with 
     * this option must be in the compiler�s source path. You specify this by using the source-path compiler option. 
     * You can use packaged and unpackaged classes. To use components in namespaces, use the include-namespaces option. 
     * If the components are in packages, ensure that you use dot-notation rather than slashes to separate package levels. 
     * This is the default option for the component compiler.
     * 
     * type: class [...]
     */
    INCLUDE_CLASSES("-include-classes"),
    
    /**
     * Adds the file to the SWC file. This option does not embed files inside the library.swf file. 
     * This is useful for adding graphics files, where you want to add non-compiled files that can 
     * be referenced in a style sheet or embedded as assets in MXML files. 
     * If you add a stylesheet that references compiled resources such as programmatic skins, use the include-stylesheet option. 
     * If you use the [Embed] syntax to add a resource to your application, 
     * you are not required to use this option to also link it into the SWC file.
     * 
     * type: name path [...]
     */
    INCLUDE_FILE("-include-file"),
    
    /**
     * If true, only manifest entries with lookupOnly=true are included in the SWC catalog.
     * 
     * type: boolean
     * default: false
     */
    INCLUDE_LOOKUP_ONLY("-include-lookup-only"),
    
    /**
     * Specifies namespace-style components in the SWC file. You specify a list of URIs to include in the SWC file. 
     * The uri argument must already be defined with the namespace option. 
     * To use components in packages, use the include-classes option.
     * 
     * type: uri [...]
     */
    INCLUDE_NAMESPACES("-include-namespaces"),
    
    /**
     * Specifies classes or directories to add to the SWC file. When specifying classes, you specify the path to the class file 
     * (for example, MyClass.as) rather than the class name itself (for example, MyClass). 
     * This lets you add classes to the SWC file that are not in the source path. 
     * In general, though, use the include-classes option, which lets you add classes that are in the source path. 
     * If you specify a directory, this option includes all files with an MXML or AS extension, and ignores all other files. 
     * If you use this option to include MXML components that are in a non-default package, you must include the source folder in the source path.
     * 
     * type: path-element
     */
    INCLUDE_SOURCES("-include-sources"),
    
    /**
     * Specifies stylesheets to add to the SWC file. 
     * This option compiles classes that are referenced by the stylesheet before including the stylesheet in the SWC file. 
     * You do not need to use this option for all stylesheets; 
     * only stylesheets that reference assets that need to be compiled such as programmatic skins or other class files. 
     * If your stylesheet does not reference compiled assets, you can use the include-file option. 
     * This option does not compile the stylesheet into a SWF file before including it in the SWC file. 
     * You compile a CSS file into a SWF file when you want to load it at run time.
     * 
     * type: namepath [...]
     */
    INCLUDE_STYLESHEET("-include-stylesheet"),
    
    //-------------//
    //---- adt ----//
    //-------------//
    
    /**
     * packages an AIR application or AIR Native Extension (ANE).
     */
    PACKAGE("-package"),
    
    /**
     * packages an AIR application as an intermediate file (AIRI), but does not sign it. AIRI files cannot be installed.
     */
    PREPARE("-prepare"),
    
    /**
     * signs an AIRI package produced with the -prepare command. 
     * The -prepare and -sign commands allow packaging and signing to be performed at different times. 
     * You can also use the -sign command to sign or resign an ANE package.
     */
    SIGN("-sign"),
    
    /**
     * applies a migration signature to a signed AIR package, which allows you to use a new or renewed code signing certificate.
     */
    MIGRATE("-migrate"),
    
    /**
     * creates a self-signed digital code signing certificate.
     */
    CERTIFICATE("-certificate"),
    
    /**
     * verifies that a digital certificate in a keystore can be accessed.
     */
    CHECKSTORE("-checkstore"),
    
    /**
     * installs an AIR application on a device or device emulator.
     */
    INSTALL_APP("-installApp"),
    
    /**
     * launches an AIR application on a device or device emulator.
     */
    LAUNCH_APP("-launchApp"),
    
    /**
     * reports the version of an AIR application currently installed on a device or device emulator.
     */
    APP_VERSION("-appVersion"),
    
    /**
     * uninstalls an AIR application from a device or device emulator
     */
    UNINSTALL_APP("-uninstallApp"),
    
    /**
     * installs the AIR runtime on a device or device emulator.
     */
    INSTALL_RUNTIME("-installRuntime"),
    
    /**
     * reports the version of the AIR runtime currently installed on a device or device emulator.
     */
    RUNTIME_VERSION("-runtimeVersion"),
    
    /**
     * uninstalls the AIR runtime currently installed from a device or device emulator.
     */
    UNINSTALL_RUNTIME("-uninstallRuntime"),

    /**
     * -C dir files_and_dirs Changes the working directory to the value of dir before processing subsequent files and
     * directories added to the application package (specified in files_and_dirs). The files or directories are added to
     * the root of the application package. The –C option can be used any number of times to include files from multiple
     * points in the file system. If a relative path is specified for dir, the path is always resolved from the original
     * working directory.
     */
    CHANGE_DIRECTORY("-C"),

    /**
     * The type of keystore, determined by the keystore implementation. The default keystore implementation included with
     * most installations of Java supports the JKS and PKCS12 types. Java 5.0 includes support for the PKCS11 type, for
     * accessing keystores on hardware tokens, and Keychain type, for accessing the Mac OS X keychain. Java 6.0 includes
     * support for the MSCAPI type (on Windows). If other JCA providers have been installed and configured, additional
     * keystore types might be available. If no keystore type is specified, the default type for the default JCA provider is used.
     */
    STORE_TYPE("-storetype"),

    /**
     * The path to the keystore file for file-based store types.
     */
    KEYSTORE("-keystore"),

    /**
     * The password required to access the keystore. If not specified, ADT prompts for the password.
     */
    STOREPASS("-storepass"),

    /**
     * DEPRECATED. This compiler option is added automatically to AIR builds.
     * -extdir dir The value of dir is the name of a directory to search for native extensions (ANE files).
     * Specify either an absolute path, or a path relative to the current directory. You can specify the -extdir option multiple times.
     */
    EXTDIR("-extdir"),

    /**
     * The SWC file containing the ActionScript code and resources for the native extension.
     */
    SWC("-swc"),

    /**
     * The name of the platform that this ANE file supports. You can include multiple -platform options, each with its own FILE_OPTIONS.
     */
    PLATFORM("-platform"),

    /**
     * The path to a platform options (platform.xml) file. Use this file to specify non-default linker options, shared
     * libraries, and third-party static libraries used by the extension.
     */
    PLATFORM_OPTIONS("-platformoptions"),

    /**
     * The path to the platform SDK for the target device:
     * Android - The AIR 2.6+ SDK includes the tools from the Android SDK needed to implement the relevant ADT commands.
     *      Only set this value to use a different version of the Android SDK. Also, the platform SDK path does not need
     *      to be supplied on the command line if the AIR_ANDROID_SDK_HOME environment variable is already set. (If both
     *      are set, then the path provided on the command line is used.)
     * iOS - The AIR SDK ships with a captive iOS SDK. The -platformsdk option lets you package applications with an
     *      external SDK so that you are not restricted to using the captive iOS SDK. For example, if you have built an
     *      extension with the latest iOS SDK, you can specify that SDK when packaging your application. Additionally,
     *      when using ADT with the iOS Simulator, you must always include the -platformsdk option, specifying the path to the iOS Simulator SDK.
     */
    PLATFORM_SDK("-platformsdk"),

    /**
     * Specify ios_simulator or the serial number of the device. The device only needs to be specified when more than one
     * Android device or emulator is attached to your computer and running. If the specified device is not connected,
     * ADT returns exit code 14: Device error. If more than one device or emulator is connected and a device is not specified,
     * ADT returns exit code 2: Usage error.
     */
    DEVICE("-device"),

    /**
     * The type of package to create. The supported package types are:
     * air — an AIR package. “air” is the default value and the -target flag does not need to be specified when creating AIR or AIRI files.
     * airn — a native application package for devices in the extended television profile.
     * ane —an AIR native extension package
     * Android package targets:
     *      apk — an Android package. A package produced with this target can only be installed on an Android device, not an emulator.
     *      apk‑captive‑runtime — an Android package that includes both the application and a captive version of the AIR runtime. A package produced with this target can only be installed on an Android device, not an emulator.
     *      apk-debug — an Android package with extra debugging information. (The SWF files in the application must also be compiled with debugging support.)
     *      apk-emulator — an Android package for use on an emulator without debugging support. (Use the apk-debug target to permit debugging on both emulators and devices.)
     *      apk-profile — an Android package that supports application performance and memory profiling.
     * iOS package targets:
     *      ipa-ad-hoc — an iOS package for ad hoc distribution.
     *      ipa-app-store — an iOS package for Apple App store distribution.
     *      ipa-debug — an iOS package with extra debugging information. (The SWF files in the application must also be compiled with debugging support.)
     *      ipa-test — an iOS package compiled without optimization or debugging information.
     *      ipa-debug-interpreter — functionally equivalent to a debug package, but compiles more quickly. However, the ActionScript bytecode is interpreted and not translated to machine code. As a result, code execution is slower in an interpreter package.
     *      ipa-debug-interpreter-simulator — functionally equivalent to ipa-debug-interpreter, but packaged for the iOS simulator. Macintosh-only. If you use this option, you must also include the -platformsdk option, specifying the path to the iOS Simulator SDK.
     *      ipa-test-interpreter — functionally equivalent to a test package, but compiles more quickly. However, the ActionScript bytecode is interpreted and not translated to machine code. As a result, code execution is slower in an interpreter package.
     *      ipa-test-interpreter-simulator — functionally equivalent to ipa-test-interpreter, but packaged for the iOS simulator. Macintosh-only. If you use this option, you must also include the -platformsdk option, specifying the path to the iOS Simulator SDK.
     * native — a native desktop installer. The type of file produced is the native installation format of the operating system on which the command is run:
     *      EXE — Windows
     *      DMG — Mac
     *      DEB — Ubuntu Linux (AIR 2.6 or earlier)
     *      RPM — Fedora or OpenSuse Linux (AIR 2.6 or earlier)
     */
    TARGET("-target"),

    /**
     * -provisioning-profile PROFILE_PATH. The path to your iOS provisioning profile.
     */
    PROVISIONING_PROFILE("-provisioning-profile"),

    /**
     * The AIR application ID of the installed application. If no application with the specified ID is installed on the
     * device, then ADT returns exit code 14: Device error.
     */
    APP_ID("-appid"),
    
    //---------------//
    //---- asdoc ----//
    //---------------//
    
    /**
     * A list of classes to document. These classes must be in the source path. This is the default option. 
     * This option works the same way as does the -include-classes option for the compc component compiler. 
     * 
     * type: path-element [...]
     */
    DOC_CLASSES("-doc-classes"),
    
    /**
     * A list of URIs to document. The classes must be in the source path.
     * You must include a URI and the location of the manifest file that defines the contents of this namespace.
     * This option works the same way as does the -include-namespaces option for the compc component compiler.
     * 
     * type: uri manifest
     */
    DOC_NAMESPACES("-doc-namespaces"),
    
    /**
     * A list of files to document. If a directory name is in the list, it is recursively searched.
     * This option works the same way as does the -include-sources option for the compc component compiler.
     * 
     * type: path-element [...]
     */
    DOC_SOURCES("-doc-sources"),
    
    /**
     * Specifies the location of the include examples used by the includeExample tag. 
     * This option specifies the root directory. 
     * The examples must be located under this directory in subdirectories that correspond to the package name of the class. 
     * For example, you specify the examples-path as c:\myExamples. 
     * For a class in the package myComp.myClass, the example must be in the directory c:\myExamples\myComp.myClass.
     * 
     * type: path-element
     */
    EXAMPLES_PATH("-examples-path"),
    
    /**
     * A list of classes not documented. You must specify individual class names. 
     * Alternatively, if the ASDoc comment for the class contains the @private tag, is not documented.
     * 
     * type: string
     */
    EXCLUDE_CLASSES("-exclude-classes"),
    
    /**
     * Whether all dependencies found by the compiler are documented. 
     * If true, the dependencies of the input classes are not documented.
     * 
     * type:boolean
     * default: false
     */
    EXCLUDE_DEPENDENCIES("-exclude-dependencies"),
    
    /**
     * Exclude a file from compilation. 
     * This option is different from the -exclude-classes option which you use to exclude a class from the output. 
     * However, the -exclude-classes option still compiles the specified class.
     * You can only exclude classes added by the doc-sources option, not classes added by the doc-namespaces or doc-classes options.
     * 
     * type: path-element [...]
     */
    EXCLUDE_SOURCES("-exclude-sources"),
    
    /**
     * The text that appears at the bottom of the HTML pages in the output documentation.
     * 
     * type: string
     */
    FOOTER("-footer"),
    
    /**
     * When true, retain the intermediate XML files created by the ASDoc tool.
     * 
     * type: boolean
     * default: false
     */
    KEEP_XML("-keep-xml"),
    
    /**
     * An integer that changes the width of the left frameset of the documentation. 
     * You can change this size to accommodate the length of your package names.
     * 
     * type: int
     * default: 210
     */
    LEFT_FRAMESET_WIDTH("-left-frameset-width"),
    
    /**
     * Ignore XHTML errors (such as a missing &lt;/p&gt; tag) and produce the ASDoc output. 
     * All errors are written to the validation_errors.log file.
     * 
     * type: none
     */
    LENIENT("-lenient"),

    /**
     * The text that appears at the top of the HTML pages in the output documentation.
     * 
     * type: string
     * default: API documentation
     */
    MAIN_TITLE("-main-title"),
    
    /**
     * Specifies an XML file containing the package descriptions.
     * 
     * type: filename
     */
    PACKAGE_DESCRIPTION_FILE("-package-description-file"),
    
    /**
     * When true, configures the ASDoc tool to generate the intermediate XML files only, 
     * and not perform the final conversion to HTML.
     * 
     * type: boolean
     * default: false
     */
    SKIP_XSL("-skip-xsl"),
    
    /**
     * The path to the ASDoc template directory. The default is the asdoc/templates directory in the ASDoc installation directory. 
     * This directory contains all the HTML, CSS, XSL, and image files used for generating the output.
     * 
     * type: string
     */
    TEMPLATES_PATH("-templates-path"),
    
    /**
     * The text that appears in the browser window in the output documentation.
     * 
     * type: string
     * default: API documentation
     */
    WINDOW_TITLE("-window-title"),

    /**
     * (iOS only, AIR 3.4 and higher) Enables the telemetry-based ActionScript sampler in iOS applications.
     * Using this flag lets you profile the application with Adobe Scout.
     * Note that using this flag will have a slight performance impact, so do not use it for production applications.
     */
    IOS_SAMPLER("-sampler"),

    /**
     * Enables to using the new non legacy compiler for packaging IPA
     *
     * target platform: IOS
     * version: AIR 14 and higher
     *
     */
    USE_LEGACY_COMPILER("-useLegacyAOT")


    private String optionName;

    private CompilerOption(String optionName) {
        this.optionName = optionName;
    }

    String getOptionName() {
        return optionName
    }

    @Override
    String toString() {
        return optionName;
    }
    
}