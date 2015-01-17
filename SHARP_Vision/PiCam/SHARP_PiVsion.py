import time
import picamera
import picamera.array
import cv2
import numpy as np

#https://programmaticponderings.wordpress.com/2013/02/09/opencv-and-cvblob-with-raspberry-pi/

best_cont = None
with picamera.PiCamera() as camera:
	camera.resolution = (640,360)
	camera.framerate = 24
	time.sleep(2)
	with picamera.array.PiRGBArray(camera) as stream:
		for image in camera.capture_continuous(stream, format='bgr', use_video_port = True):
			startLoopTime = time.time()
			hsv = cv2.cvtColor(stream.array, cv2.COLOR_BGR2HSV)
			threshold = cv2.inRange(hsv, np.array((0,80,80)), np.array((20,255,255)))
			thresholdProjected = threshold.copy()

			contours, heirarchy = cv2.findContours(threshold, cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)


			max_area = 0
			for cont in contours:
				area = cv2.contourArea(cont)
				if area > max_area:
					max_area = area
					best_cont = cont
			if best_cont is not None:
				M = cv2.moments(best_cont)
				cx, cy = int(M['m10']/M['m00']), int(M['m01']/M['m00'])
				cv2.circle(stream.array,(cx,cy), 5, 255,-1)

			cv2.imshow('Real',stream.array)
			cv2.imshow('Threshold',thresholdProjected)
			stream.truncate(0)
			elapsedTime = ((time.time()) - startLoopTime) *1000
			print("Start Time: %d, End Time: %d, Elapsed: %d"%(startLoopTime, time.time(),elapsedTime))
			if cv2.waitKey(33) == 27:
				break

	cv2.destroyAllWindows()
	
