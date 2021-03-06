/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.util;

import org.apache.hadoop.HadoopVersionAnnotation;
import org.apache.hadoop.metrics.util.MBeanUtil;

import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

/**
 * This class finds the package info for Hadoop and the HadoopVersionAnnotation
 * information.
 */
public class VersionInfo implements VersionInfoMBean {
  private static Package myPackage;
  private static HadoopVersionAnnotation version;
  
  static {
    myPackage = HadoopVersionAnnotation.class.getPackage();
    version = myPackage.getAnnotation(HadoopVersionAnnotation.class);
  }

  /**
   * Get the meta-data for the Hadoop package.
   * @return
   */
  static Package getPackage() {
    return myPackage;
  }
  
  /**
   * Get the Hadoop version.
   * @return the Hadoop version string, eg. "0.6.3-dev"
   */
  public static String getVersion() {
    return version != null ? version.version() : "Unknown";
  }
  
  /**
   * Get the subversion revision number for the root directory
   * @return the revision number, eg. "451451"
   */
  public static String getRevision() {
    return version != null ? version.revision() : "Unknown";
  }
  
  /**
   * The date that Hadoop was compiled.
   * @return the compilation date in unix date format
   */
  public static String getDate() {
    return version != null ? version.date() : "Unknown";
  }
  
  /**
   * The user that compiled Hadoop.
   * @return the username of the user
   */
  public static String getUser() {
    return version != null ? version.user() : "Unknown";
  }
  
  /**
   * Get the subversion URL for the root Hadoop directory.
   */
  public static String getUrl() {
    return version != null ? version.url() : "Unknown";
  }
  
  /**
   * Returns the buildVersion which includes version, 
   * revision, user and date. 
   */
  public static String getBuildVersion(){
    return VersionInfo.getVersion() + 
    " from " + VersionInfo.getRevision() +
    " by " + VersionInfo.getUser() + 
    " on " + VersionInfo.getDate();
  }

  public String version() {
    return "Hadoop " + VersionInfo.getVersion();
  }

  public String subversion() {
    return "Subversion " + VersionInfo.getUrl() +
           " -r " + VersionInfo.getRevision();
  }

  public String compiledby() {
    return "Compiled by " + VersionInfo.getUser() +
           " on " + VersionInfo.getDate();
  }

  public static ObjectName registerJMX(String daemon) {
    StandardMBean versionBean;
    ObjectName versionBeanName = null;
    try {
      versionBean = new StandardMBean(new VersionInfo(),
                                          VersionInfoMBean.class);
      versionBeanName =
        MBeanUtil.registerMBean(daemon, "Version", versionBean);
    } catch (NotCompliantMBeanException e) {
      e.printStackTrace();
    }

    return versionBeanName;
  }

  private static String valueForm(String v) {
    return "<value>" + v + "</value>";
  }

  public static void main(String[] args) {
    System.out.println("Hadoop " + valueForm(getVersion()));
    System.out.println("Subversion " + valueForm(getUrl() + " -r " + getRevision()));
    System.out.println("Compiled by " + valueForm(getUser() + " on " + getDate()));
    System.out.println("Build Version " + valueForm(getBuildVersion()));
  }
}
