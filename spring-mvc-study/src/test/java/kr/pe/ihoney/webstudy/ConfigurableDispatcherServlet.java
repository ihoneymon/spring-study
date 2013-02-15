package kr.pe.ihoney.webstudy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class ConfigurableDispatcherServlet extends DispatcherServlet {

	private static final long serialVersionUID = 6912163749981815137L;

	/**
	 * 설정방법을 확장해서 클래스와 XML 파일 두가 장법을 모두 지원하게 만들었다.
	 */
	@Setter
	private Class<?>[] classes;

	@Setter
	private String[] locations;

	@Getter
	private ModelAndView modelAndView;

	public ConfigurableDispatcherServlet(String[] locations) {
		this.locations = locations;
	}

	public ConfigurableDispatcherServlet(Class<?>[] classes) {
		this.classes = classes;
	}

	public void setRelativeLocation(Class clazz, String... relativeLocations) {
		String[] locations = new String[relativeLocations.length];
		String currentPath = ClassUtils.classPackageAsResourcePath(clazz) + "/";
		log.debug(currentPath);

		int relativeLocationsLength = relativeLocations.length;
		for (int i = 0; i < relativeLocationsLength; i++) {
			locations[i] = currentPath = relativeLocations[i];
		}

		this.setLocations(locations);
	}

	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		modelAndView = null;
		super.service(req, res);
	}

	protected WebApplicationContext createWebApplicationContext(
			ApplicationContext parent) {
		AbstractRefreshableWebApplicationContext wac = new AbstractRefreshableWebApplicationContext() {

			@Override
			protected void loadBeanDefinitions(
					DefaultListableBeanFactory beanFactory)
					throws BeansException, IOException {
				if (locations != null) {
					XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(
							beanFactory);
					xmlReader.loadBeanDefinitions(locations);
				}
				if (classes != null) {
					AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(
							beanFactory);
					reader.register(classes);
				}
			}
		};

		wac.setServletContext(getServletContext());
		wac.setServletConfig(getServletConfig());
		wac.refresh();

		return wac;
	}

	protected void render(ModelAndView modelAndView, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		this.modelAndView = modelAndView;
		super.render(modelAndView, req, res);
	}
}
