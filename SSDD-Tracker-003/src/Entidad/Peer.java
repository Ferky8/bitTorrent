package Entidad;

public class Peer {
	//The 20 byte sha1 hash of the bencoded form of the info value from the metainfo file.
	//Note that this is a substring of the metainfo file. The info-hash must be the hash 
	//of the encoded form as found in the .torrent file, regardless of it being invalid.
	//This value will almost certainly have to be escaped
	private String infoHash;
	
	//A string of length 20 which this downloader uses as its id. Each downloader generates
	//its own id at random at the start of a new download.
	//This value will also almost certainly have to be escaped.
	private String peerId;

	//An optional parameter giving the IP (or dns name) which this peer is at.
	//Generally used for the origin if it's on the same machine as the tracker.
	private String ip;
	
	//The port number this peer is listening on.
	private int port;
	
	//The total amount uploaded so far, encoded in base ten ascii
	private int uploaded;
	
	//The total amount downloaded so far, encoded in base ten ascii
	private int downloaded;
	
	//The number of bytes this peer still has to download, encoded in base ten ascii.
	//Note that this can't be computed from downloaded and the file length since it 
	//might be a resume, and there's a chance that some of the downloaded data failed
	//an integrity check and had to be re-downloaded
	private int left;
		
	public String getInfoHash() {
		return infoHash;
	}

	public void setInfoHash(String infoHash) {
		this.infoHash = infoHash;
	}

	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getUploaded() {
		return uploaded;
	}

	public void setUploaded(int uploaded) {
		this.uploaded = uploaded;
	}

	public int getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(int downloaded) {
		this.downloaded = downloaded;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		
		if (this.peerId != null && !this.peerId.trim().isEmpty()) {
			result.append("peer_id: ");
			result.append(this.peerId);
		}
		
		if (this.ip != null && !this.ip.trim().isEmpty()) {
			result.append(" - ip: ");
			result.append(this.ip);
		}
		
		if (this.port != 0) {
			result.append(" - port: ");
			result.append(this.port);
		}
		
		if (this.infoHash != null && !this.infoHash.trim().isEmpty()) {
			result.append(" - info_hash: ");
			result.append(this.infoHash);
		}
		
		return result.toString();
	}
}