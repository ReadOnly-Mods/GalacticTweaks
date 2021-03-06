package net.romvoid95.galactic.core;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.romvoid95.api.config.annotation.GCTFeature;
import net.romvoid95.api.config.def.Category;
import net.romvoid95.api.config.values.OptArrayDouble;
import net.romvoid95.api.config.values.OptArrayInteger;
import net.romvoid95.api.config.values.OptArrayString;
import net.romvoid95.api.config.values.OptBoolean;
import net.romvoid95.api.config.values.OptDouble;
import net.romvoid95.api.config.values.OptInteger;
import net.romvoid95.api.config.values.OptString;
import net.romvoid95.api.config.values.OptValue;
import net.romvoid95.api.versioning.Version;
import net.romvoid95.galactic.Info;

public abstract class ReadOnlyConfig {

	private File					configFile;
	private ConfigVersion			Configversion;
	private final Class<?>			clazz;
	protected List<OptValue>		properties		= new ArrayList<>();
	protected List<Field>			features		= new ArrayList<>();
	protected List<String>			propOrder		= new ArrayList<>();
	protected List<Category>		configCats		= new ArrayList<>();
	public static final Category	FEATURES		= Category.of("allfeatures").setRequiredRestarts(false, true);
	public static final Category	FEATURE_OPTS	= Category.of("featureopts");

	private Configuration config;

	protected ReadOnlyConfig(Class<?> clazz) {
		this.clazz = clazz;
		MinecraftForge.EVENT_BUS.register(this);

		List<Field> subClazzFields = setFeatureFields(clazz.getDeclaredFields());
		subClazzFields.forEach(f -> addFeatOpt(f));
	}

	public File getConfigFile() {
		return configFile;
	}

	public ConfigVersion getConfigversion() {
		return Configversion;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	protected void setConfigversion(ConfigVersion configversion) {
		Configversion = configversion;
	}

	protected void addConfigCat(Category configCat) {
		this.configCats.add(configCat);
	}

	public boolean hasProperty(OptValue prop) {
		return this.properties.stream().anyMatch(property -> property.category().contentEquals(prop.category()) && property.key().contentEquals(prop.key()));
	}

	protected final OptBoolean addProperty(OptBoolean property) {
		this.addPropToList(property.key());
		this.addProp(property);
		return property;
	}

	protected final OptInteger addProperty(OptInteger property) {
		this.addPropToList(property.key());
		this.addProp(property);
		return property;
	}

	protected final OptString addProperty(OptString property) {
		this.addPropToList(property.key());
		this.addProp(property);
		return property;
	}

	protected final OptArrayInteger addProperty(OptArrayInteger property) {
		this.addPropToList(property.key());
		this.addProp(property);
		return property;
	}

	protected final OptArrayDouble addProperty(OptArrayDouble property) {
		this.addPropToList(property.key());
		this.addProp(property);
		return property;
	}
	
	protected final OptArrayString addProperty(OptArrayString property) {
		this.addPropToList(property.key());
		this.addProp(property);
		return property;
	}

	public void loadConfig() {

		config = initConfig();

		try {
			config.load();

			for (Field field : this.features) {
				GCTFeature currFeature = field.getAnnotation(GCTFeature.class);
				String key = currFeature.featureClass().getSimpleName().toLowerCase();
				try {
					//currFeature.featureClass().newInstance();
					field.setBoolean(this.clazz, config.get(FEATURES.get(), key, false).getBoolean());
				} catch (IllegalAccessException ex) {
					GCTLog.error("Field \"{}\" is not accessible?", field.getName());
					GCTLog.catching(ex);
				}
			}

			for (OptValue prop : this.properties) {

				try {
					switch (prop.getType()) {
						case INTEGER:
							OptInteger propInt = (OptInteger) prop;
							int intVal;
							if (propInt.hasRange()) {
								intVal = config.get(propInt.category(), propInt.key(), propInt.get(), propInt.comment(), propInt.min(), propInt.max()).setLanguageKey(propInt.langKey()).getInt();
							} else {
								intVal = config.get(propInt.category(), propInt.key(), propInt.get(), propInt.comment()).setLanguageKey(propInt.langKey()).getInt();
							}
							propInt.set(intVal);
							break;

						case INTEGER_ARRAY:
							OptArrayInteger propIntArray = (OptArrayInteger) prop;
							int[] intArray;
							if (propIntArray.hasRange()) {
								intArray = config.get(propIntArray.category(), propIntArray.key(), propIntArray.get(), propIntArray.comment(), propIntArray.min(), propIntArray.max()).setLanguageKey(propIntArray.langKey()).getIntList();
							} else {
								intArray = config.get(propIntArray.category(), propIntArray.key(), propIntArray.get(), propIntArray.comment()).setLanguageKey(propIntArray.langKey()).getIntList();
							}
							propIntArray.set(intArray);
							break;

						case DOUBLE:
							OptDouble propDouble = (OptDouble) prop;
							double doubleVal;
							if (propDouble.hasRange()) {
								doubleVal = config.get(propDouble.category(), propDouble.key(), propDouble.get(), propDouble.comment(), propDouble.min(), propDouble.max()).setLanguageKey(propDouble.langKey()).getDouble();
							} else {
								doubleVal = config.get(propDouble.category(), propDouble.key(), propDouble.get(), propDouble.comment()).setLanguageKey(propDouble.langKey()).getDouble();
							}
							propDouble.set(doubleVal);
							break;

						case DOUBLE_ARRAY:
							OptArrayDouble propDoubleArray = (OptArrayDouble) prop;
							double[] doubleArrayVal;
							if (propDoubleArray.hasRange()) {
								doubleArrayVal = config.get(propDoubleArray.category(), propDoubleArray.key(), propDoubleArray.get(), propDoubleArray.comment(), propDoubleArray.min(), propDoubleArray.max()).setLanguageKey(propDoubleArray.langKey()).getDoubleList();
							} else {
								doubleArrayVal = config.get(propDoubleArray.category(), propDoubleArray.key(), propDoubleArray.get(), propDoubleArray.comment()).setLanguageKey(propDoubleArray.langKey()).getDoubleList();
							}
							propDoubleArray.set(doubleArrayVal);
							break;

						case BOOLEAN:
							OptBoolean propBool = (OptBoolean) prop;
							propBool.set(config.getBoolean(propBool.key(), propBool.category(), propBool.get(), propBool.comment(), propBool.langKey()));
							break;

						case STRING:
							OptString propString = (OptString) prop;
							String Stringvalue;
							if (propString.needsValidation()) {
								Stringvalue = config.getString(propString.key(), propString.category(), propString.get(), propString.comment(), propString.getValidValues(), propString.getValidValuesDisplay(), propString.langKey());
							} else {
								Stringvalue = config.getString(propString.key(), propString.category(), propString.get(), propString.comment(), propString.langKey());
							}
							propString.set(Stringvalue);
							break;
						case STRING_ARRAY:
							OptArrayString propStringArray = (OptArrayString) prop;
							propStringArray.set(config.get(propStringArray.category(), propStringArray.key(), propStringArray.get(), propStringArray.comment()).setLanguageKey(propStringArray.langKey()).getStringList());
							break;
					}
				} catch (Exception e) {
					GCTLog.error("Issue with Prop: {} of type {}", prop.key(), prop.getType().name());
					GCTLog.catching(e);
				} finally {
					config.setCategoryPropertyOrder(prop.category(), this.propOrder);
					config.setCategoryLanguageKey(prop.category(), prop.getConfigCat().getLangKey());
					config.setCategoryRequiresMcRestart(prop.category(), prop.getConfigCat().requiresMCRestart);
					config.setCategoryRequiresWorldRestart(prop.category(), prop.getConfigCat().requiresWorldRestart);
				}
			}
		} catch (Exception ignored) {
			GCTLog.error("Uh, we had a problem loading config: {}", config.getConfigFile());
		}
		saveConfig();
	}

	protected Configuration initConfig() {
		return new Configuration(this.configFile, this.Configversion.toString());
	}

	protected void saveConfig() {
		if (config.hasChanged()) {
			config.save();
		}
	}

	public Configuration get() {
		return config;
	}

	public ConfigCategory getCategory(Category category) {
		return config.getCategory(category.get());
	}

	protected List<IConfigElement> getElements() {
		List<IConfigElement> list = new ArrayList<>();
		ConfigCategory allFeatures = getCategory(FEATURES).setLanguageKey("galactictweaks.config.gui.cat.allfeatures");
		ConfigCategory featureOpts = getCategory(FEATURE_OPTS).setLanguageKey("galactictweaks.config.gui.cat.featureopts");
		list.add(new ConfigElement(allFeatures));
		list.add(new ConfigElement(featureOpts));
		return list;
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equalsIgnoreCase(Info.ID)) {
			saveConfig();
			loadConfig();
		}
	}

	public static class ConfigVersion extends Version {
		public static final ConfigVersion NULL_VERSION = new ConfigVersion("0.0.0");

		public ConfigVersion(String version) {
			super(version);
		}

		public ConfigVersion(Version version) {
			super(version.toString());
		}

		public ConfigVersion(int major, int minor, int patch, int build) {
			super(of(major) + "." + of(minor) + "." + of(patch) + "." + of(build));
		}

		private static String of(int val) {
			return String.valueOf(val);
		}

		public static boolean isVersionLessOrEqual(ConfigVersion comparate1, ConfigVersion comparate2) {
			Version c1 = new Version(comparate1.toString());
			Version c2 = new Version(comparate2.toString());
			return c1.isEqualTo(c2);
		}
	}

	// PRIVATE UTILTITY METHODS //

	private List<Field> setFeatureFields(Field[] fields) {
		List<Field> flds = new ArrayList<>();
		for (Field f : fields) {
			if (f.isAnnotationPresent(GCTFeature.class)) {
				flds.add(f);
			}
		}
		return flds;
	}

	private void addPropToList(String propName) {
		for (String name : this.propOrder) {
			if (name.contentEquals(propName)) {
				this.propOrder.remove(name);
				break;
			}
		}
		this.propOrder.add(propName);
	}

	private void addProp(OptValue property) {
		for (OptValue prop : this.properties) {
			if (prop.key().contentEquals(property.key())) {
				this.properties.remove(prop);
				break;
			}
		}
		this.properties.add(property);
	}

	private void addFeatOpt(Field feature) {
		for (Field feat : this.features) {
			if (feat.getName().contentEquals(feature.getName())) {
				this.features.remove(feat);
				break;
			}
		}
		this.features.add(feature);
	}
}
