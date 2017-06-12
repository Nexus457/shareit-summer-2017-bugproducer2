package edu.hm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import edu.hm.bugproducer.restAPI.media.MediaService;
import edu.hm.bugproducer.restAPI.media.MediaServiceImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * Context Listener to enable usage of google guice together with jersey.
 */
public class ShareitServletContextListener
        extends GuiceServletContextListener {
    private static final Injector INJECTOR
            = Guice.createInjector(new ServletModule() {
        @Override
        protected void configureServlets() {
            bind(MediaService.class).to(MediaServiceImpl.class);
        }
    });

    /**
     * This method is only required for the HK2-Guice-Bridge in the
     * Application class.
     *
     * @return Injector instance.
     */
    static Injector getInjectorInstance() {
        return INJECTOR;
    }

    @Override
    public Injector getInjector() {
        return INJECTOR;
    }

}