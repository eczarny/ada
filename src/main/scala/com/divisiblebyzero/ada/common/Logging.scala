package com.divisiblebyzero.ada.common

import org.apache.log4j.Logger

trait Logging {
  val loggerName = this.getClass.getName
  lazy val logger = Logger.getLogger(loggerName)

  def trace(message: String) { logger.trace(message) }
  def trace(message: String, e: Throwable) { logger.trace(message, e) }

  def debug(message: String) { logger.debug(message) }
  def debug(message: String, e: Throwable) { logger.debug(message, e) }

  def info(message: String) { logger.info(message) }
  def info(message: String, e: Throwable) { logger.info(message, e) }

  def warn(message: String) { logger.warn(message) }
  def warn(message: String, e: Throwable) { logger.warn(message, e) }

  def error(message: String) { logger.error(message) }
  def error(message: String, e: Throwable) { logger.error(message, e) }

  def fatal(message: String) { logger.fatal(message) }
  def fatal(message: String, e: Throwable) { logger.fatal(message, e) }
}
